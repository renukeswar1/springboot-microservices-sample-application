package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoProcessingEvent {
    private Long videoId;
    private String originalFilename;
    private String s3Key;
    private String targetFormat; // 1080p, 720p, 480p
    private VideoProcessingStatus status;
    private String processedS3Key; // Key for processed video
    private String errorMessage;
    private long processingStartTime;
    private long processingEndTime;
    private int retryCount;
}