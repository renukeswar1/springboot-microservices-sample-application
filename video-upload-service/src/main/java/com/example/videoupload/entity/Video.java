package com.example.videoupload.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String originalFilename;
    
    @Column(nullable = false)
    private String originalS3Key;
    
    @Column(nullable = false)
    private long fileSizeBytes;
    
    @Column(nullable = false)
    private String fileFormat;
    
    private String uploadedBy;
    
    @Column(nullable = false)
    private LocalDateTime uploadedAt;
    
    @Enumerated(EnumType.STRING)
    private VideoStatus status = VideoStatus.UPLOADED;
    
    // URLs for processed videos
    private String url1080p;
    private String url720p;
    private String url480p;
    
    public enum VideoStatus {
        UPLOADED,
        PROCESSING,
        COMPLETED,
        FAILED
    }
}