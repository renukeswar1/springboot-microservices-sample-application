package com.example.videoupload.repository;

import com.example.videoupload.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    
    List<Video> findByUploadedByOrderByUploadedAtDesc(String uploadedBy);
    
    List<Video> findByStatusOrderByUploadedAtDesc(Video.VideoStatus status);
    
    List<Video> findAllByOrderByUploadedAtDesc();
}