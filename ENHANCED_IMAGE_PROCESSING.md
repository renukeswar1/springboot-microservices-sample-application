# Enhanced Image Upload and Processing System

## Overview

This enhanced image upload system provides comprehensive image processing capabilities for the movie management application. It processes uploaded images into multiple variants, extracts metadata, and provides a scalable, asynchronous architecture.

## Architecture

### Current Image Upload Flow

1. **Synchronous Upload** (Movie API)
   - User uploads image via `POST /api/movies` (multipart/form-data)
   - Movie metadata saved to PostgreSQL database
   - Image data sent to Kafka topic `movie-images`

2. **Asynchronous Processing** (Image Upload Service)
   - ImageUploadConsumer receives Kafka events
   - **NEW**: Image processing into multiple variants
   - **NEW**: Metadata extraction
   - S3 storage with organized folder structure
   - Movie record updated with image URLs

### Enhanced Features

#### 🖼️ **Multi-Variant Image Processing**
- **Original**: Uploaded image as-is
- **Large**: 1920x1080 max (95% quality) - for zoom functionality
- **Medium**: 800x600 max (85% quality) - for standard display
- **Thumbnail**: 200x200 max (75% quality) - for previews

#### 📊 **Metadata Extraction**
- Image dimensions (width, height, aspect ratio)
- File size and format
- EXIF data (camera info, location, etc.)
- Creation timestamps

#### 🗂️ **Organized S3 Storage**
```
movie-image-upload-renu/
└── movies/
    └── {movieId}/
        ├── original.jpg
        ├── large.jpg
        ├── medium.jpg
        └── thumbnail.jpg
```

## API Endpoints

### Image Upload Service (Port 8094)

#### Health Check
```bash
GET /api/images/health
```

#### Validate Image
```bash
POST /api/images/validate
Content-Type: multipart/form-data
Body: file=<image-file>
```

Response:
```json
{
  "valid": true,
  "filename": "sample.jpg",
  "size": 1024000,
  "metadata": {
    "width": 1920,
    "height": 1080,
    "aspectRatio": 1.777,
    "format": "JPEG",
    "fileSize": 1024000
  }
}
```

#### Get Supported Formats
```bash
GET /api/images/formats
```

Response:
```json
{
  "supportedFormats": ["JPEG", "JPG", "PNG", "GIF", "BMP", "WEBP"],
  "maxFileSize": "10MB",
  "variants": ["original", "large", "medium", "thumbnail"],
  "variantSizes": {
    "large": "1920x1080 max",
    "medium": "800x600 max",
    "thumbnail": "200x200 max"
  }
}
```

#### Generate S3 Key
```bash
GET /api/images/s3-key?movieId=123&filename=image.jpg&variant=large
```

### Movie API (Port 8080)

#### Upload Movie with Image
```bash
POST /api/movies
Content-Type: multipart/form-data
Body: 
  title=Movie Title
  file=<image-file>
```

## Technology Stack

### Core Technologies
- **Spring Boot 3.3.x**: Application framework
- **Apache Kafka**: Asynchronous message processing
- **AWS S3**: Cloud storage
- **PostgreSQL**: Metadata storage

### Image Processing Libraries
- **Thumbnailator**: High-quality image resizing
- **Metadata Extractor**: EXIF and metadata extraction
- **Java ImageIO**: Core image handling

## Configuration

### Application Properties (`application.yml`)
```yaml
spring:
  application:
    name: imageupload-service
  kafka:
    bootstrap-servers: localhost:29092
    consumer:
      group-id: movie-images-group
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
    properties:
      spring.json.trusted.packages: "org.example.models"

kafka:
  topic:
    name: movie-images

server:
  port: 8094

# AWS S3 Configuration (via environment variables)
# AWS_ACCESS_KEY_ID
# AWS_SECRET_ACCESS_KEY
# AWS_REGION
```

## Processing Flow Details

### 1. Image Validation
- Check file format (JPEG, PNG, GIF, BMP, WEBP)
- Validate file size limits
- Verify image integrity

### 2. Metadata Extraction
- Dimensions and aspect ratio
- File format and compression
- EXIF data (camera, GPS, timestamps)
- Color profile information

### 3. Multi-Variant Generation
- **Original**: Preserve uploaded image
- **Large**: High-quality for zoom functionality
- **Medium**: Standard display size
- **Thumbnail**: Quick loading previews

### 4. S3 Storage Organization
- Folder structure by movie ID
- Consistent naming convention
- Appropriate content-type headers
- Public read access for web display

### 5. Database Updates
- Primary image URL (large variant)
- All variant URLs (future enhancement)
- Metadata storage (future enhancement)

## Synchronous vs Asynchronous Processing

### Current Implementation: **Asynchronous**

**Benefits:**
- ✅ Non-blocking user experience
- ✅ Scalable processing
- ✅ Fault tolerance with Kafka
- ✅ Retry capabilities
- ✅ Can handle traffic spikes

**Flow:**
1. User uploads → immediate response
2. Background processing → multiple variants
3. Database update → completion notification

### Progress Tracking

To track processing progress, you can:

1. **Check Movie Record**: Poll the movie API for image URL updates
2. **Status Endpoints**: (Future enhancement) dedicated progress tracking
3. **WebSocket Notifications**: (Future enhancement) real-time updates

## Zoom and Display Features

### Image Variants for Different Use Cases

1. **Thumbnail (200x200)**: 
   - Gallery views
   - Quick previews
   - Mobile optimization

2. **Medium (800x600)**:
   - Standard web display
   - Responsive design
   - Fast loading

3. **Large (1920x1080)**:
   - Full-screen display
   - Zoom functionality
   - High-quality viewing

4. **Original**:
   - Maximum quality
   - Download option
   - Archival purposes

### Frontend Implementation Guide

```javascript
// Example: Progressive image loading
const imageUrls = {
  thumbnail: "https://bucket.s3.aws.com/movies/123/thumbnail.jpg",
  medium: "https://bucket.s3.aws.com/movies/123/medium.jpg", 
  large: "https://bucket.s3.aws.com/movies/123/large.jpg"
};

// Load thumbnail first, then upgrade
function loadProgressiveImage(container) {
  const img = new Image();
  img.src = imageUrls.thumbnail;
  img.onload = () => {
    container.appendChild(img);
    // Load higher quality version
    loadHigherQuality(container, imageUrls.medium);
  };
}

// Zoom functionality
function enableZoom(imageElement) {
  imageElement.addEventListener('click', () => {
    // Switch to large variant for zoom
    showZoomModal(imageUrls.large);
  });
}
```

## Error Handling

### Processing Failures
- Invalid image format
- Corrupted files
- S3 upload failures
- Metadata extraction errors

### Retry Logic
- Kafka consumer automatic retry
- Exponential backoff
- Dead letter queue for failed messages

### Monitoring
- Processing success/failure rates
- Image variant generation times
- S3 upload performance
- Kafka consumer lag

## Performance Considerations

### Optimization Strategies
1. **Parallel Processing**: Generate variants concurrently
2. **Quality Settings**: Balance size vs quality
3. **Caching**: Cache frequently accessed variants
4. **CDN Integration**: Serve images via CloudFront

### Scaling
- **Horizontal**: Multiple image processing workers
- **Vertical**: Increase worker memory/CPU
- **Storage**: S3 auto-scaling
- **Queue**: Kafka partition scaling

## Future Enhancements

### Short Term
- [ ] Progress tracking API
- [ ] Webhook notifications
- [ ] Image variant management endpoints
- [ ] Batch processing capabilities

### Long Term
- [ ] AI-powered image optimization
- [ ] Automatic format conversion (WebP)
- [ ] CDN integration
- [ ] Advanced metadata search
- [ ] Image comparison and similarity
- [ ] Watermarking capabilities

## Testing

### Unit Tests
```bash
cd imageupload-service
mvn test
```

### Integration Tests
```bash
# Test image upload flow
curl -X POST http://localhost:8080/api/movies \
  -H "Content-Type: multipart/form-data" \
  -F "title=Test Movie" \
  -F "file=@test-image.jpg"

# Validate image
curl -X POST http://localhost:8094/api/images/validate \
  -H "Content-Type: multipart/form-data" \
  -F "file=@test-image.jpg"

# Check health
curl http://localhost:8094/api/images/health
```

## Troubleshooting

### Common Issues

1. **Kafka Connection**: Check bootstrap servers configuration
2. **S3 Permissions**: Verify AWS credentials and bucket access
3. **Image Format**: Ensure supported formats (JPEG, PNG, etc.)
4. **Memory Issues**: Increase JVM heap for large images

### Debug Logging
```yaml
logging:
  level:
    com.example.taskmgmtkafkaconsumer: DEBUG
    org.springframework.kafka: DEBUG
```

## Monitoring and Metrics

### Key Metrics
- Image processing time per variant
- Success/failure rates
- Queue depth and lag
- S3 upload performance
- Memory and CPU usage

### Health Checks
- Service health endpoint
- Kafka connectivity
- S3 accessibility
- Database connectivity

This enhanced image upload system provides a robust, scalable foundation for handling movie images with multiple variants, comprehensive metadata, and excellent user experience through progressive loading and zoom capabilities.