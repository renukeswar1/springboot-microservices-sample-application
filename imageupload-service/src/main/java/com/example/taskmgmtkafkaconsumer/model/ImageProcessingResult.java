package com.example.taskmgmtkafkaconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageProcessingResult {
    private Long movieId;
    private String originalFilename;
    private Map<String, String> imageUrls; // variant -> S3 URL mapping
    private Map<String, Object> metadata;
    private ProcessingStatus status;
    private String errorMessage;
    
    public enum ProcessingStatus {
        PROCESSING, COMPLETED, FAILED
    }
    
    // Constructor for successful processing
    public ImageProcessingResult(Long movieId, String originalFilename, 
                               Map<String, String> imageUrls, Map<String, Object> metadata) {
        this.movieId = movieId;
        this.originalFilename = originalFilename;
        this.imageUrls = imageUrls;
        this.metadata = metadata;
        this.status = ProcessingStatus.COMPLETED;
    }
    
    // Constructor for failed processing
    public ImageProcessingResult(Long movieId, String originalFilename, String errorMessage) {
        this.movieId = movieId;
        this.originalFilename = originalFilename;
        this.status = ProcessingStatus.FAILED;
        this.errorMessage = errorMessage;
    }
}