package com.example.taskmgmtkafkaconsumer.service;

import com.example.taskmgmtkafkaconsumer.entity.MovieImageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieApiClient {
    private final RestTemplate restTemplate;
    private final String movieApiUrl = "http://localhost:8080/api/movies"; // Update with your API Gateway URL or service discovery

    public MovieApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void updateMovieImage(Long movieId, String imageUrl) {
        String url = movieApiUrl + "/" + movieId + "/image";
        MovieImageRequest request = new MovieImageRequest(imageUrl);
        restTemplate.put(url, request);
    }
}
