package com.renu.movieapi.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateMovieRequest {
    private Long id;

    @NotBlank
    private String title;

    private MultipartFile file; // Field to handle the uploaded file
}

