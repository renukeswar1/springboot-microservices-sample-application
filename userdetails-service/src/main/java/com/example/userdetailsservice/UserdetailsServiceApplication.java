package com.example.userdetailsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserdetailsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserdetailsServiceApplication.class, args);
    }

}
