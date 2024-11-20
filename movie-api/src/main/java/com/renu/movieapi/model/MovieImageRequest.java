package com.renu.movieapi.model;

public class MovieImageRequest {
    private String imageUrl;

    public MovieImageRequest(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

