package com.example.videoupload.service;

import com.example.videoupload.entity.Video;
import com.example.videoupload.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.VideoUploadEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoUploadService {
    
    private final VideoRepository videoRepository;
    private final S3Client s3Client;
    private final KafkaTemplate<String, VideoUploadEvent> kafkaTemplate;
    
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    
    @Value("${kafka.topic.video-upload}")
    private String videoUploadTopic;
    
    private static final String[] SUPPORTED_VIDEO_FORMATS = {
        "mp4", "avi", "mkv", "mov", "wmv", "flv", "webm"
    };
    
    public List<Video> getAllVideos() {
        return videoRepository.findAllByOrderByUploadedAtDesc();
    }
    
    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findById(id);
    }
    
    public List<Video> getVideosByUser(String username) {
        return videoRepository.findByUploadedByOrderByUploadedAtDesc(username);
    }
    
    public Video uploadVideo(String title, MultipartFile file, String uploadedBy) throws IOException {
        // Validate file
        validateVideoFile(file);
        
        // Save video metadata to database
        Video video = new Video();
        video.setTitle(title);
        video.setOriginalFilename(file.getOriginalFilename());
        video.setFileSizeBytes(file.getSize());
        video.setFileFormat(getFileExtension(file.getOriginalFilename()));
        video.setUploadedBy(uploadedBy);
        video.setUploadedAt(LocalDateTime.now());
        video.setStatus(Video.VideoStatus.UPLOADED);
        
        // Generate unique S3 key
        String s3Key = generateS3Key(video.getOriginalFilename());
        video.setOriginalS3Key(s3Key);
        
        // Save to database first
        Video savedVideo = videoRepository.save(video);
        
        try {
            // Upload to S3
            uploadToS3(s3Key, file);
            log.info("Video uploaded to S3 successfully: {}", s3Key);
            
            // Send message to Kafka for processing
            VideoUploadEvent event = new VideoUploadEvent(
                savedVideo.getId(),
                savedVideo.getOriginalFilename(),
                file.getBytes(),
                savedVideo.getFileFormat(),
                savedVideo.getFileSizeBytes(),
                uploadedBy,
                System.currentTimeMillis()
            );
            
            kafkaTemplate.send(videoUploadTopic, event);
            log.info("Video upload event sent to Kafka: {}", savedVideo.getId());
            
        } catch (Exception e) {
            log.error("Failed to upload video to S3 or send to Kafka", e);
            // Update status to failed
            savedVideo.setStatus(Video.VideoStatus.FAILED);
            videoRepository.save(savedVideo);
            throw new RuntimeException("Failed to upload video", e);
        }
        
        return savedVideo;
    }
    
    public void updateVideoProcessingStatus(Long videoId, Video.VideoStatus status) {
        Optional<Video> videoOpt = videoRepository.findById(videoId);
        if (videoOpt.isPresent()) {
            Video video = videoOpt.get();
            video.setStatus(status);
            videoRepository.save(video);
        }
    }
    
    public void updateVideoUrls(Long videoId, String url1080p, String url720p, String url480p) {
        Optional<Video> videoOpt = videoRepository.findById(videoId);
        if (videoOpt.isPresent()) {
            Video video = videoOpt.get();
            video.setUrl1080p(url1080p);
            video.setUrl720p(url720p);
            video.setUrl480p(url480p);
            video.setStatus(Video.VideoStatus.COMPLETED);
            videoRepository.save(video);
        }
    }
    
    private void validateVideoFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename is required");
        }
        
        String extension = getFileExtension(filename).toLowerCase();
        boolean isSupported = false;
        for (String supportedFormat : SUPPORTED_VIDEO_FORMATS) {
            if (supportedFormat.equals(extension)) {
                isSupported = true;
                break;
            }
        }
        
        if (!isSupported) {
            throw new IllegalArgumentException("Unsupported video format: " + extension);
        }
        
        // Check file size (max 500MB)
        if (file.getSize() > 500 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 500MB");
        }
    }
    
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }
    
    private String generateS3Key(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFilename);
        return String.format("videos/uploads/%s.%s", uuid, extension);
    }
    
    private void uploadToS3(String s3Key, MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();
        
        PutObjectResponse response = s3Client.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );
        
        log.info("S3 upload response ETag: {}", response.eTag());
    }
}