package com.example.videoworker.consumer;

import com.example.videoworker.service.VideoProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.VideoUploadEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoUploadConsumer {
    
    private final VideoProcessingService videoProcessingService;
    
    @KafkaListener(
        topics = "${kafka.topic.video-upload}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleVideoUpload(
            @Payload VideoUploadEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        log.info("Received video upload event - Video ID: {}, Topic: {}, Partition: {}, Offset: {}", 
                event.getVideoId(), topic, partition, offset);
        
        try {
            // Generate S3 key from the video upload event
            String s3Key = generateS3Key(event.getOriginalFilename(), event.getVideoId());
            
            // Start video processing
            videoProcessingService.processVideo(
                event.getVideoId(),
                event.getOriginalFilename(),
                s3Key
            );
            
            // Manual acknowledgment after successful processing initiation
            acknowledgment.acknowledge();
            log.info("Successfully initiated processing for video ID: {}", event.getVideoId());
            
        } catch (Exception e) {
            log.error("Failed to process video upload event for video ID: {}", event.getVideoId(), e);
            // Don't acknowledge on failure - message will be retried or sent to DLQ
            throw new RuntimeException("Failed to process video upload event", e);
        }
    }
    
    private String generateS3Key(String originalFilename, Long videoId) {
        String extension = getFileExtension(originalFilename);
        return String.format("videos/uploads/%d.%s", videoId, extension);
    }
    
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "mp4";
    }
}