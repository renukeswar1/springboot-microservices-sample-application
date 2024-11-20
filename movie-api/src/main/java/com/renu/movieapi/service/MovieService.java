package com.renu.movieapi.service;

import com.renu.movieapi.model.Movie;
import com.renu.movieapi.model.MovieImageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    List<Movie> getMovies();



    Movie saveMovie(Movie movie, MultipartFile file) throws IOException;

    void deleteMovie(Movie movie);

    void updateMovieImage(Long id, MovieImageRequest movieImageRequest);
}
