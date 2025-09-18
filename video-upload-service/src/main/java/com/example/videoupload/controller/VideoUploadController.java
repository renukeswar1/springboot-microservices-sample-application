package com.example.videoupload.controller;

import com.example.videoupload.entity.Video;
import com.example.videoupload.service.VideoUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Video Upload", description = "Video upload and management APIs")
public class VideoUploadController {
    
    private final VideoUploadService videoUploadService;
    
    @PostMapping("/upload")
    @Operation(summary = "Upload a video", description = "Upload a video file for processing")
    public ResponseEntity<?> uploadVideo(
            @Parameter(description = "Video title") @RequestParam("title") String title,
            @Parameter(description = "Video file") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Username of uploader") @RequestParam(value = "uploadedBy", required = false) String uploadedBy) {
        
        try {
            if (uploadedBy == null || uploadedBy.isEmpty()) {
                uploadedBy = "anonymous";
            }
            
            Video video = videoUploadService.uploadVideo(title, file, uploadedBy);
            log.info("Video uploaded successfully: {}", video.getId());
            
            return ResponseEntity.ok(video);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid video upload request: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to upload video", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload video: " + e.getMessage());
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all videos", description = "Retrieve all uploaded videos")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoUploadService.getAllVideos();
        return ResponseEntity.ok(videos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get video by ID", description = "Retrieve a specific video by its ID")
    public ResponseEntity<?> getVideoById(@PathVariable Long id) {
        Optional<Video> video = videoUploadService.getVideoById(id);
        if (video.isPresent()) {
            return ResponseEntity.ok(video.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{username}")
    @Operation(summary = "Get videos by user", description = "Retrieve all videos uploaded by a specific user")
    public ResponseEntity<List<Video>> getVideosByUser(@PathVariable String username) {
        List<Video> videos = videoUploadService.getVideosByUser(username);
        return ResponseEntity.ok(videos);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update video status", description = "Update the processing status of a video")
    public ResponseEntity<?> updateVideoStatus(
            @PathVariable Long id,
            @RequestParam Video.VideoStatus status) {
        
        try {
            videoUploadService.updateVideoProcessingStatus(id, status);
            return ResponseEntity.ok("Video status updated successfully");
        } catch (Exception e) {
            log.error("Failed to update video status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update video status: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/urls")
    @Operation(summary = "Update processed video URLs", description = "Update the URLs for processed video formats")
    public ResponseEntity<?> updateVideoUrls(
            @PathVariable Long id,
            @RequestParam(required = false) String url1080p,
            @RequestParam(required = false) String url720p,
            @RequestParam(required = false) String url480p) {
        
        try {
            videoUploadService.updateVideoUrls(id, url1080p, url720p, url480p);
            return ResponseEntity.ok("Video URLs updated successfully");
        } catch (Exception e) {
            log.error("Failed to update video URLs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update video URLs: " + e.getMessage());
        }
    }
}