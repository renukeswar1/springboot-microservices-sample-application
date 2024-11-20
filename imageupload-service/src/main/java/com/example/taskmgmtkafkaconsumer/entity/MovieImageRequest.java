package com.example.taskmgmtkafkaconsumer.entity;


public class MovieImageRequest {
    private String imageUrl;
    public MovieImageRequest() {}
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
