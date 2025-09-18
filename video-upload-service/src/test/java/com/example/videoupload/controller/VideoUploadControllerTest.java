package com.example.videoupload.controller;

import com.example.videoupload.service.VideoUploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VideoUploadController.class)
class VideoUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoUploadService videoUploadService;

    @Test
    void testGetAllVideos() throws Exception {
        mockMvc.perform(get("/api/videos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUploadVideoWithValidFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        mockMvc.perform(multipart("/api/videos/upload")
                .file(file)
                .param("title", "Test Video")
                .param("uploadedBy", "testuser"))
                .andExpect(status().isOk());
    }

    @Test
    void testUploadVideoWithInvalidFileType() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-document.txt",
                "text/plain",
                "test content".getBytes()
        );

        mockMvc.perform(multipart("/api/videos/upload")
                .file(file)
                .param("title", "Test Video")
                .param("uploadedBy", "testuser"))
                .andExpect(status().isBadRequest());
    }
}