package com.example.taskmgmtkafkaconsumer.service;

import com.example.taskmgmtkafkaconsumer.entity.MovieImageRequest;
import com.example.taskmgmtkafkaconsumer.model.ImageProcessingResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class MovieApiClient {
    private final RestTemplate restTemplate;
    private final String movieApiUrl = "http://localhost:8080/api/movies"; // Update with your API Gateway URL or service discovery

    public MovieApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void updateMovieImage(Long movieId, String imageUrl) {
        String url = movieApiUrl + "/" + movieId + "/image";
        MovieImageRequest request = new MovieImageRequest(imageUrl);
        try {
            restTemplate.put(url, request);
            log.info("Successfully updated movie {} with image URL: {}", movieId, imageUrl);
        } catch (Exception e) {
            log.error("Failed to update movie {} with image URL: {}", movieId, imageUrl, e);
            throw e;
        }
    }

    /**
     * Update movie with multiple image variants and metadata
     */
    public void updateMovieWithProcessingResult(ImageProcessingResult result) {
        try {
            // For now, we'll update with the large variant as the primary image
            // In a full implementation, you'd extend the movie API to handle multiple variants
            String primaryImageUrl = result.getImageUrls().get("large");
            if (primaryImageUrl == null) {
                primaryImageUrl = result.getImageUrls().get("original");
            }
            
            if (primaryImageUrl != null) {
                updateMovieImage(result.getMovieId(), primaryImageUrl);
            }
            
            // TODO: Add API endpoint to store image variants and metadata
            // updateMovieImageVariants(result.getMovieId(), result.getImageUrls(), result.getMetadata());
            
            log.info("Successfully updated movie {} with processing result. Variants: {}", 
                    result.getMovieId(), result.getImageUrls().keySet());
        } catch (Exception e) {
            log.error("Failed to update movie {} with processing result", result.getMovieId(), e);
            throw e;
        }
    }
}
