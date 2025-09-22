package com.example.taskmgmtkafkaconsumer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageProcessingServiceTest {

    private ImageProcessingService imageProcessingService;

    @BeforeEach
    void setUp() {
        imageProcessingService = new ImageProcessingService();
    }

    @Test
    void testIsValidImageFormat() {
        assertTrue(imageProcessingService.isValidImageFormat("test.jpg"));
        assertTrue(imageProcessingService.isValidImageFormat("test.jpeg"));
        assertTrue(imageProcessingService.isValidImageFormat("test.png"));
        assertTrue(imageProcessingService.isValidImageFormat("test.gif"));
        assertTrue(imageProcessingService.isValidImageFormat("test.bmp"));
        assertTrue(imageProcessingService.isValidImageFormat("test.webp"));
        
        assertFalse(imageProcessingService.isValidImageFormat("test.txt"));
        assertFalse(imageProcessingService.isValidImageFormat("test.pdf"));
        assertFalse(imageProcessingService.isValidImageFormat("test"));
        assertFalse(imageProcessingService.isValidImageFormat(null));
    }

    @Test
    void testGenerateS3Key() {
        String s3Key = imageProcessingService.generateS3Key(123L, "test.jpg", "thumbnail");
        assertEquals("movies/123/thumbnail.jpg", s3Key);
        
        s3Key = imageProcessingService.generateS3Key(456L, "image.png", "large");
        assertEquals("movies/456/large.png", s3Key);
    }

    @Test
    void testProcessImage() throws IOException {
        // Create a test image
        byte[] testImageData = createTestImage();
        
        Map<String, byte[]> processedImages = imageProcessingService.processImage(testImageData, "test.jpg");
        
        assertNotNull(processedImages);
        assertEquals(4, processedImages.size());
        assertTrue(processedImages.containsKey("original"));
        assertTrue(processedImages.containsKey("large"));
        assertTrue(processedImages.containsKey("medium"));
        assertTrue(processedImages.containsKey("thumbnail"));
        
        // Verify original is unchanged
        assertArrayEquals(testImageData, processedImages.get("original"));
        
        // Verify other variants exist and are different
        assertNotNull(processedImages.get("large"));
        assertNotNull(processedImages.get("medium"));
        assertNotNull(processedImages.get("thumbnail"));
    }

    @Test
    void testExtractImageMetadata() throws IOException {
        byte[] testImageData = createTestImage();
        
        Map<String, Object> metadata = imageProcessingService.extractImageMetadata(testImageData, "test.jpg");
        
        assertNotNull(metadata);
        assertTrue(metadata.containsKey("width"));
        assertTrue(metadata.containsKey("height"));
        assertTrue(metadata.containsKey("aspectRatio"));
        assertTrue(metadata.containsKey("filename"));
        assertTrue(metadata.containsKey("fileSize"));
        assertTrue(metadata.containsKey("format"));
        
        assertEquals("test.jpg", metadata.get("filename"));
        assertEquals("JPEG", metadata.get("format"));
        assertEquals(100, metadata.get("width"));
        assertEquals(100, metadata.get("height"));
        assertEquals(1.0, metadata.get("aspectRatio"));
    }

    private byte[] createTestImage() throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
}