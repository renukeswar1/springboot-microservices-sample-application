package com.renu.movieapi.service;

import com.renu.movieapi.exception.MovieNotFoundException;
import com.renu.movieapi.model.Movie;
import com.renu.movieapi.model.MovieImageRequest;
import com.renu.movieapi.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.example.models.MovieMessageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    @Value("${kafka.topic.name}")
    private String topicName;

    private final MovieRepository movieRepository;
    private final KafkaTemplate<String, MovieMessageEvent> kafkaTemplate;

    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAllByOrderByTitle();
    }

    @Override
    public Movie saveMovie(Movie movie, MultipartFile file) throws IOException {
        Movie movie1 = movieRepository.save(movie);
        MovieMessageEvent event = new MovieMessageEvent(movie1.getId(),file.getOriginalFilename(),file.getBytes());
        kafkaTemplate.send(topicName, event);
        return movie1;
    }

    @Override
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }

    @Override
    public void updateMovieImage(Long id, MovieImageRequest movieImageRequest) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            Movie movie1 = movie.get();
            movie1.setImageURL(movieImageRequest.getImageUrl());
            movieRepository.save(movie1);
        }else {
            throw new MovieNotFoundException("Movie not found");
        }
    }
}
