package com.example.videoupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VideoUploadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoUploadServiceApplication.class, args);
    }
}