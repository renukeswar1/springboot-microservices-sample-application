package com.example.taskmgmtkafkaconsumer.consumer;

import com.example.taskmgmtkafkaconsumer.model.ImageProcessingResult;
import com.example.taskmgmtkafkaconsumer.service.ImageProcessingService;
import com.example.taskmgmtkafkaconsumer.service.MovieApiClient;
import lombok.extern.slf4j.Slf4j;
import org.example.models.MovieMessageEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ImageUploadConsumer {

    private final S3Client s3Client;
    private final String bucketName = "movie-image-upload-renu";
    private final MovieApiClient movieApiClient;
    private final ImageProcessingService imageProcessingService;

    public ImageUploadConsumer(S3Client s3Client, MovieApiClient movieApiClient, ImageProcessingService imageProcessingService) {
        this.s3Client = s3Client;
        this.movieApiClient = movieApiClient;
        this.imageProcessingService = imageProcessingService;
    }

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleImageUpload(MovieMessageEvent event) {
        log.info("Received image upload event for movie ID: {}, filename: {}", 
                event.getMovieId(), event.getOriginalFilename());
        
        try {
            // Validate image format
            if (!imageProcessingService.isValidImageFormat(event.getOriginalFilename())) {
                throw new IllegalArgumentException("Unsupported image format: " + event.getOriginalFilename());
            }

            // Extract metadata from original image
            Map<String, Object> metadata = imageProcessingService.extractImageMetadata(
                    event.getFileData(), event.getOriginalFilename());
            log.info("Extracted metadata for {}: dimensions={}x{}, size={} bytes", 
                    event.getOriginalFilename(), 
                    metadata.get("width"), 
                    metadata.get("height"), 
                    metadata.get("fileSize"));

            // Process image into multiple variants
            Map<String, byte[]> processedImages = imageProcessingService.processImage(
                    event.getFileData(), event.getOriginalFilename());

            // Upload all variants to S3
            Map<String, String> imageUrls = new HashMap<>();
            for (Map.Entry<String, byte[]> entry : processedImages.entrySet()) {
                String variant = entry.getKey();
                byte[] imageData = entry.getValue();
                
                String s3Key = imageProcessingService.generateS3Key(
                        event.getMovieId(), event.getOriginalFilename(), variant);
                
                String imageUrl = uploadToS3(s3Key, imageData, event.getOriginalFilename());
                imageUrls.put(variant, imageUrl);
                
                log.info("Uploaded {} variant to S3: {}", variant, imageUrl);
            }

            // Create processing result
            ImageProcessingResult result = new ImageProcessingResult(
                    event.getMovieId(), 
                    event.getOriginalFilename(), 
                    imageUrls, 
                    metadata);

            // Update movie with processing result
            movieApiClient.updateMovieWithProcessingResult(result);
            
            log.info("Successfully processed and uploaded image for movie ID: {} with {} variants", 
                    event.getMovieId(), imageUrls.size());

        } catch (Exception e) {
            log.error("Failed to process image upload for movie ID: {}, filename: {}", 
                    event.getMovieId(), event.getOriginalFilename(), e);
            
            // Create failed result
            ImageProcessingResult failedResult = new ImageProcessingResult(
                    event.getMovieId(), event.getOriginalFilename(), e.getMessage());
            
            // TODO: Send failure notification or retry logic
        }
    }

    /**
     * Upload image data to S3 and return the public URL
     */
    private String uploadToS3(String s3Key, byte[] imageData, String originalFilename) {
        try {
            // Determine content type based on file extension
            String contentType = getContentType(originalFilename);
            
            PutObjectResponse response = s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .contentType(contentType)
                            .contentLength((long) imageData.length)
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(imageData)
            );
            
            String imageURL = String.format("https://%s.s3.amazonaws.com/%s", bucketName, s3Key);
            log.debug("Uploaded to S3: {} (ETag: {})", imageURL, response.eTag());
            
            return imageURL;
        } catch (Exception e) {
            log.error("Failed to upload to S3: {}", s3Key, e);
            throw new RuntimeException("S3 upload failed for key: " + s3Key, e);
        }
    }

    /**
     * Determine content type based on file extension
     */
    private String getContentType(String filename) {
        if (filename == null) return "image/jpeg";
        
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowerFilename.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerFilename.endsWith(".bmp")) {
            return "image/bmp";
        } else if (lowerFilename.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "image/jpeg"; // Default for JPG/JPEG
        }
    }
}
