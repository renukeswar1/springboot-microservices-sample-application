package com.example.videoworker.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.VideoFormat;
import org.example.models.VideoProcessingEvent;
import org.example.models.VideoProcessingStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingService {
    
    private final S3Client s3Client;
    private final KafkaTemplate<String, VideoProcessingEvent> kafkaTemplate;
    
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    
    @Value("${kafka.topic.video-processing}")
    private String videoProcessingTopic;
    
    @Value("${processing.max-concurrent-jobs:5}")
    private int maxConcurrentJobs;
    
    @Value("${processing.timeout-minutes:30}")
    private int timeoutMinutes;
    
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
    @CircuitBreaker(name = "video-processing", fallbackMethod = "fallbackProcessVideo")
    @Retry(name = "video-processing")
    public void processVideo(Long videoId, String originalFilename, String s3Key) {
        log.info("Starting video processing for video ID: {}", videoId);
        
        // Process each format asynchronously
        CompletableFuture<Void> future1080p = processVideoFormatAsync(videoId, originalFilename, s3Key, VideoFormat.FORMAT_1080P);
        CompletableFuture<Void> future720p = processVideoFormatAsync(videoId, originalFilename, s3Key, VideoFormat.FORMAT_720P);
        CompletableFuture<Void> future480p = processVideoFormatAsync(videoId, originalFilename, s3Key, VideoFormat.FORMAT_480P);
        
        // Wait for all processing to complete
        CompletableFuture.allOf(future1080p, future720p, future480p)
            .thenRun(() -> {
                log.info("All video processing completed for video ID: {}", videoId);
                sendProcessingStatusUpdate(videoId, VideoProcessingStatus.COMPLETED, null);
            })
            .exceptionally(throwable -> {
                log.error("Video processing failed for video ID: {}", videoId, throwable);
                sendProcessingStatusUpdate(videoId, VideoProcessingStatus.FAILED, throwable.getMessage());
                return null;
            });
    }
    
    private CompletableFuture<Void> processVideoFormatAsync(Long videoId, String originalFilename, String originalS3Key, VideoFormat format) {
        return CompletableFuture.runAsync(() -> {
            try {
                processVideoFormat(videoId, originalFilename, originalS3Key, format);
            } catch (Exception e) {
                log.error("Failed to process video format {} for video ID: {}", format.getName(), videoId, e);
                throw new RuntimeException(e);
            }
        }, executorService);
    }
    
    private void processVideoFormat(Long videoId, String originalFilename, String originalS3Key, VideoFormat format) throws Exception {
        log.info("Processing video ID: {} to format: {}", videoId, format.getName());
        
        // Create temporary directories
        Path tempDir = Files.createTempDirectory("video-processing-" + videoId + "-" + format.getName());
        Path inputFile = tempDir.resolve("input." + getFileExtension(originalFilename));
        Path outputFile = tempDir.resolve("output_" + format.getName() + ".mp4");
        
        try {
            // Download original video from S3
            downloadFromS3(originalS3Key, inputFile);
            
            // Process video using FFmpeg (simulated)
            processVideoWithFFmpeg(inputFile, outputFile, format);
            
            // Upload processed video back to S3
            String processedS3Key = generateProcessedS3Key(originalFilename, format);
            uploadToS3(processedS3Key, outputFile);
            
            // Send processing event
            VideoProcessingEvent event = new VideoProcessingEvent();
            event.setVideoId(videoId);
            event.setOriginalFilename(originalFilename);
            event.setS3Key(originalS3Key);
            event.setTargetFormat(format.getName());
            event.setStatus(VideoProcessingStatus.COMPLETED);
            event.setProcessedS3Key(processedS3Key);
            event.setProcessingEndTime(System.currentTimeMillis());
            
            kafkaTemplate.send(videoProcessingTopic, event);
            
            log.info("Successfully processed video ID: {} to format: {}", videoId, format.getName());
            
        } finally {
            // Cleanup temporary files
            cleanupTempFiles(tempDir);
        }
    }
    
    private void downloadFromS3(String s3Key, Path localFile) throws IOException {
        log.debug("Downloading from S3: {} to {}", s3Key, localFile);
        
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();
        
        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
             OutputStream outputStream = Files.newOutputStream(localFile)) {
            
            s3Object.transferTo(outputStream);
        }
        
        log.debug("Downloaded {} bytes from S3", Files.size(localFile));
    }
    
    private void uploadToS3(String s3Key, Path localFile) throws IOException {
        log.debug("Uploading to S3: {} from {}", s3Key, localFile);
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType("video/mp4")
                .contentLength(Files.size(localFile))
                .build();
        
        PutObjectResponse response = s3Client.putObject(
                putObjectRequest,
                RequestBody.fromFile(localFile)
        );
        
        log.debug("Uploaded to S3 with ETag: {}", response.eTag());
    }
    
    private void processVideoWithFFmpeg(Path inputFile, Path outputFile, VideoFormat format) throws Exception {
        log.debug("Processing video with FFmpeg: {} -> {} ({})", inputFile, outputFile, format.getName());
        
        // Simulate FFmpeg processing - In real implementation, this would call FFmpeg
        String command = String.format(
            "ffmpeg -i %s -vf scale=%d:%d -b:v %dk -y %s",
            inputFile.toString(),
            format.getWidth(),
            format.getHeight(),
            format.getBitrate(),
            outputFile.toString()
        );
        
        log.info("Simulated FFmpeg command: {}", command);
        
        // Simulate processing time based on video format
        long processingTimeMs = switch (format) {
            case FORMAT_1080P -> 10000; // 10 seconds for 1080p
            case FORMAT_720P -> 7000;   // 7 seconds for 720p
            case FORMAT_480P -> 5000;   // 5 seconds for 480p
        };
        
        Thread.sleep(processingTimeMs);
        
        // Create a dummy output file for simulation
        Files.copy(inputFile, outputFile);
        
        log.debug("FFmpeg processing completed for format: {}", format.getName());
    }
    
    private String generateProcessedS3Key(String originalFilename, VideoFormat format) {
        String baseName = removeFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("videos/processed/%s_%s_%s.mp4", baseName, format.getName(), timestamp);
    }
    
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "mp4";
    }
    
    private String removeFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(0, lastDotIndex);
        }
        return filename;
    }
    
    private void cleanupTempFiles(Path tempDir) {
        try {
            Files.walk(tempDir)
                .sorted((a, b) -> b.compareTo(a)) // Delete files before directories
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        log.warn("Failed to delete temp file: {}", path, e);
                    }
                });
        } catch (IOException e) {
            log.warn("Failed to cleanup temp directory: {}", tempDir, e);
        }
    }
    
    private void sendProcessingStatusUpdate(Long videoId, VideoProcessingStatus status, String errorMessage) {
        VideoProcessingEvent event = new VideoProcessingEvent();
        event.setVideoId(videoId);
        event.setStatus(status);
        event.setErrorMessage(errorMessage);
        event.setProcessingEndTime(System.currentTimeMillis());
        
        kafkaTemplate.send(videoProcessingTopic, event);
    }
    
    // Fallback method for circuit breaker
    public void fallbackProcessVideo(Long videoId, String originalFilename, String s3Key, Exception ex) {
        log.error("Circuit breaker activated for video processing. Video ID: {}", videoId, ex);
        sendProcessingStatusUpdate(videoId, VideoProcessingStatus.FAILED, "Service temporarily unavailable");
    }
}