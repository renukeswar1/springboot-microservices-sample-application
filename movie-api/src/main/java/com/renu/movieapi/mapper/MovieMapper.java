package com.renu.movieapi.mapper;

import com.renu.movieapi.model.Movie;
import com.renu.movieapi.rest.dto.CreateMovieRequest;
import com.renu.movieapi.rest.dto.MovieDto;

public interface MovieMapper {

    Movie toMovie(CreateMovieRequest createMovieRequest);

    MovieDto toMovieDto(Movie movie);
}