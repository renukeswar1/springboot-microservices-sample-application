package com.example.taskmgmtkafkaconsumer.consumer;


import com.example.taskmgmtkafkaconsumer.service.MovieApiClient;
import org.example.models.MovieMessageEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.file.Paths;

@Service
public class ImageUploadConsumer {

    private final S3Client s3Client;
    private final String bucketName = "movie-image-upload-renu";
    private final MovieApiClient movieApiClient;

    public ImageUploadConsumer(S3Client s3Client, MovieApiClient movieApiClient) {
        this.s3Client = s3Client;
        this.movieApiClient = movieApiClient;
    }


    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleImageUpload(MovieMessageEvent event) {
        try {
            // Construct the S3 key (file path in the bucket)
            String key = "movies/" + event.getOriginalFilename();

            // Upload the file to S3
            PutObjectResponse response = s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType("image/jpeg") // Specify the content type
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(event.getFileData())
            );
            String imageURL = "https://"+bucketName+".s3.amazonaws.com/"+key;
            movieApiClient.updateMovieImage(event.getMovieId(),imageURL );
            // Log the S3 upload response
            System.out.println("Image uploaded to S3: " + response.eTag());
        } catch (Exception e) {
            // Log the error and handle retries if necessary
            System.err.println("Failed to upload image: " + e.getMessage());
        }
    }
}
