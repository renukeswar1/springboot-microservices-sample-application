# Video Processing Platform

A microservices-based video processing platform built with Spring Boot that handles video uploads and transcodes them into multiple formats (1080p, 720p, 480p) for optimized delivery.

## Architecture Overview

This platform is designed to handle spiky traffic loads cost-effectively by implementing:

- **Asynchronous processing** with Kafka messaging
- **Circuit breaker patterns** for resilience
- **Auto-scaling capabilities** for worker services
- **Cloud storage** integration with AWS S3
- **Service discovery** with Eureka

## Services

### 1. Video Upload Service (`video-upload-service`)
- **Port**: 8085
- **Purpose**: Handles video file uploads and metadata management
- **Features**:
  - File validation (supports mp4, avi, mkv, mov, wmv, flv, webm)
  - Size limits (max 500MB)
  - S3 storage integration
  - Kafka message publishing
  - REST API for video management

**Key Endpoints**:
- `POST /api/videos/upload` - Upload a video file
- `GET /api/videos` - Get all videos
- `GET /api/videos/{id}` - Get video by ID
- `GET /api/videos/user/{username}` - Get videos by user

### 2. Video Worker Service (`video-worker-service`)
- **Port**: 8086
- **Purpose**: Processes uploaded videos into multiple formats
- **Features**:
  - Asynchronous video processing
  - Multiple format transcoding (1080p, 720p, 480p)
  - Circuit breaker for fault tolerance
  - Retry mechanisms
  - Concurrent processing
  - Health monitoring

### 3. Gateway Service (`gateway`)
- **Port**: 8090
- **Purpose**: API Gateway for routing requests to appropriate services
- **Features**:
  - Load balancing
  - CORS configuration
  - Service discovery integration

### 4. Eureka Server (`eureka-server`)
- **Port**: 8761
- **Purpose**: Service discovery and registration

## Technology Stack

- **Framework**: Spring Boot 3.3.x
- **Message Queue**: Apache Kafka
- **Storage**: AWS S3
- **Database**: PostgreSQL (metadata)
- **Service Discovery**: Netflix Eureka
- **Circuit Breaker**: Resilience4j
- **Video Processing**: FFmpeg (simulated)
- **Container Support**: Docker Ready

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Kafka
- PostgreSQL
- AWS S3 credentials (for storage)

### Running the Platform

1. **Start Infrastructure Services**:
   ```bash
   docker-compose up -d postgres kafka zookeeper
   ```

2. **Start Eureka Server**:
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```

3. **Start Video Upload Service**:
   ```bash
   cd video-upload-service
   mvn spring-boot:run
   ```

4. **Start Video Worker Service(s)**:
   ```bash
   cd video-worker-service
   mvn spring-boot:run
   ```

5. **Start Gateway Service**:
   ```bash
   cd gateway
   mvn spring-boot:run
   ```

### Configuration

Update the following configuration files:

**AWS S3 Configuration** (in `application.yml`):
```yaml
aws:
  s3:
    bucket-name: your-video-bucket
    region: your-aws-region
```

**Kafka Configuration**:
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:29092
```

**Database Configuration**:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/moviedb
    username: postgres
    password: your-password
```

## API Usage Examples

### Upload a Video
```bash
curl -X POST \
  http://localhost:8090/api/videos/upload \
  -F "file=@/path/to/video.mp4" \
  -F "title=My Awesome Video" \
  -F "uploadedBy=john_doe"
```

### Get All Videos
```bash
curl http://localhost:8090/api/videos
```

### Get Video by ID
```bash
curl http://localhost:8090/api/videos/1
```

## Scaling for High Traffic

The platform is designed to handle traffic spikes through:

### 1. Horizontal Scaling
- **Video Workers**: Run multiple instances of the video-worker-service
- **Upload Service**: Run multiple instances behind the gateway
- **Kafka Partitioning**: Scale message processing across partitions

### 2. Cost Optimization
- **Circuit Breakers**: Prevent cascade failures during high load
- **Async Processing**: Decouple upload from processing for better user experience
- **Retry Logic**: Handle transient failures gracefully
- **Dead Letter Queues**: Isolate permanently failed messages

### 3. Auto-scaling Configuration
```yaml
processing:
  max-concurrent-jobs: 5  # Adjust based on instance capacity
  timeout-minutes: 30     # Processing timeout
  
resilience4j:
  circuitbreaker:
    instances:
      video-processing:
        failure-rate-threshold: 50
        minimum-number-of-calls: 5
```

## Monitoring and Health Checks

### Health Endpoints
- Video Upload Service: `http://localhost:8085/actuator/health`
- Video Worker Service: `http://localhost:8086/actuator/health`
- Gateway: `http://localhost:8090/actuator/health`

### Metrics
- Processing queue depth
- Success/failure rates
- Processing times
- Circuit breaker states

## Video Processing Flow

1. **Upload**: User uploads video via API Gateway
2. **Storage**: Video stored in S3, metadata in PostgreSQL
3. **Queue**: Upload event sent to Kafka
4. **Processing**: Worker picks up event and processes video
5. **Transcoding**: Create multiple format versions (1080p, 720p, 480p)
6. **Storage**: Processed videos uploaded back to S3
7. **Notification**: Processing completion event sent
8. **Update**: Video metadata updated with processed URLs

## Development

### Building Services
```bash
# Build shared models
cd shared-models
mvn clean install

# Build all services
mvn clean compile -DskipTests
```

### Running Tests
```bash
mvn test
```

## Future Enhancements

- [ ] Real FFmpeg integration
- [ ] Video thumbnail generation
- [ ] Progress tracking for uploads
- [ ] Webhook notifications
- [ ] Video analytics
- [ ] CDN integration
- [ ] Subtitle/caption support
- [ ] Live streaming support