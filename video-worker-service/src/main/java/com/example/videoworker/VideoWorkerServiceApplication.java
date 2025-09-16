package com.example.videoworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VideoWorkerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoWorkerServiceApplication.class, args);
    }
}