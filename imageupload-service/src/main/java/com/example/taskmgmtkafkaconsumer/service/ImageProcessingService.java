package com.example.taskmgmtkafkaconsumer.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ImageProcessingService {

    /**
     * Process uploaded image to create multiple variants:
     * - Original (uploaded as-is)
     * - Large (1920x1080 max)
     * - Medium (800x600 max) 
     * - Thumbnail (200x200 max)
     */
    public Map<String, byte[]> processImage(byte[] originalImageData, String originalFilename) throws IOException {
        Map<String, byte[]> processedImages = new HashMap<>();
        
        // Read original image
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageData));
        if (originalImage == null) {
            throw new IOException("Invalid image format: " + originalFilename);
        }
        
        log.info("Processing image: {} - Original dimensions: {}x{}", 
                originalFilename, originalImage.getWidth(), originalImage.getHeight());
        
        // Store original
        processedImages.put("original", originalImageData);
        
        // Create large variant (max 1920x1080, maintain aspect ratio)
        byte[] largeImage = resizeImage(originalImageData, 1920, 1080, 0.95f);
        processedImages.put("large", largeImage);
        
        // Create medium variant (max 800x600, maintain aspect ratio)
        byte[] mediumImage = resizeImage(originalImageData, 800, 600, 0.85f);
        processedImages.put("medium", mediumImage);
        
        // Create thumbnail (max 200x200, maintain aspect ratio)
        byte[] thumbnailImage = resizeImage(originalImageData, 200, 200, 0.75f);
        processedImages.put("thumbnail", thumbnailImage);
        
        log.info("Successfully processed image {} into {} variants", originalFilename, processedImages.size());
        return processedImages;
    }

    /**
     * Extract metadata from image (dimensions, format, EXIF data)
     */
    public Map<String, Object> extractImageMetadata(byte[] imageData, String originalFilename) {
        Map<String, Object> metadata = new HashMap<>();
        
        try {
            // Basic image info using ImageIO
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
            if (image != null) {
                metadata.put("width", image.getWidth());
                metadata.put("height", image.getHeight());
                metadata.put("aspectRatio", (double) image.getWidth() / image.getHeight());
            }
            
            // Extract EXIF and other metadata using metadata-extractor
            Metadata exifMetadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imageData));
            for (Directory directory : exifMetadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    metadata.put(directory.getName() + "." + tag.getTagName(), tag.getDescription());
                }
            }
            
            // File info
            metadata.put("filename", originalFilename);
            metadata.put("fileSize", imageData.length);
            metadata.put("format", getImageFormat(originalFilename));
            
        } catch (Exception e) {
            log.warn("Failed to extract metadata from image {}: {}", originalFilename, e.getMessage());
            // Still return basic info
            metadata.put("filename", originalFilename);
            metadata.put("fileSize", imageData.length);
            metadata.put("format", getImageFormat(originalFilename));
        }
        
        return metadata;
    }

    /**
     * Resize image maintaining aspect ratio with specified quality
     */
    private byte[] resizeImage(byte[] originalImageData, int maxWidth, int maxHeight, float quality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        Thumbnails.of(new ByteArrayInputStream(originalImageData))
                .size(maxWidth, maxHeight)
                .outputQuality(quality)
                .outputFormat("JPEG")
                .toOutputStream(outputStream);
        
        return outputStream.toByteArray();
    }

    /**
     * Determine image format from filename
     */
    private String getImageFormat(String filename) {
        if (filename == null) return "unknown";
        
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
            return "JPEG";
        } else if (lowerFilename.endsWith(".png")) {
            return "PNG";
        } else if (lowerFilename.endsWith(".gif")) {
            return "GIF";
        } else if (lowerFilename.endsWith(".bmp")) {
            return "BMP";
        } else if (lowerFilename.endsWith(".webp")) {
            return "WEBP";
        } else {
            return "unknown";
        }
    }

    /**
     * Validate if file is a supported image format
     */
    public boolean isValidImageFormat(String filename) {
        String format = getImageFormat(filename);
        return !format.equals("unknown");
    }

    /**
     * Generate file key for S3 storage based on variant
     */
    public String generateS3Key(Long movieId, String originalFilename, String variant) {
        String extension = getFileExtension(originalFilename);
        return String.format("movies/%d/%s.%s", movieId, variant, extension);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}