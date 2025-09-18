package com.example.taskmgmtkafkaconsumer.controller;

import com.example.taskmgmtkafkaconsumer.service.ImageProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@Slf4j
public class ImageController {

    private final ImageProcessingService imageProcessingService;

    public ImageController(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "Image Upload Service");
        return ResponseEntity.ok(status);
    }

    /**
     * Validate image format endpoint
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("valid", false);
                response.put("message", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }
            
            boolean isValid = imageProcessingService.isValidImageFormat(file.getOriginalFilename());
            response.put("valid", isValid);
            response.put("filename", file.getOriginalFilename());
            response.put("size", file.getSize());
            
            if (isValid) {
                // Extract metadata for valid images
                byte[] imageData = file.getBytes();
                Map<String, Object> metadata = imageProcessingService.extractImageMetadata(
                        imageData, file.getOriginalFilename());
                response.put("metadata", metadata);
            } else {
                response.put("message", "Unsupported image format");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error validating image: {}", e.getMessage());
            response.put("valid", false);
            response.put("message", "Error processing file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get supported image formats
     */
    @GetMapping("/formats")
    public ResponseEntity<Map<String, Object>> getSupportedFormats() {
        Map<String, Object> response = new HashMap<>();
        response.put("supportedFormats", new String[]{"JPEG", "JPG", "PNG", "GIF", "BMP", "WEBP"});
        response.put("maxFileSize", "10MB");
        response.put("variants", new String[]{"original", "large", "medium", "thumbnail"});
        
        Map<String, String> variantSizes = new HashMap<>();
        variantSizes.put("large", "1920x1080 max");
        variantSizes.put("medium", "800x600 max");
        variantSizes.put("thumbnail", "200x200 max");
        response.put("variantSizes", variantSizes);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Generate S3 key for a given movie ID and variant
     */
    @GetMapping("/s3-key")
    public ResponseEntity<Map<String, String>> generateS3Key(
            @RequestParam Long movieId,
            @RequestParam String filename,
            @RequestParam(defaultValue = "original") String variant) {
        
        Map<String, String> response = new HashMap<>();
        try {
            String s3Key = imageProcessingService.generateS3Key(movieId, filename, variant);
            response.put("s3Key", s3Key);
            response.put("movieId", movieId.toString());
            response.put("variant", variant);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating S3 key: {}", e.getMessage());
            response.put("error", "Failed to generate S3 key: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}