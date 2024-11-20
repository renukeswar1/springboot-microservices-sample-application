package com.renu.movieapi.mapper;

import com.renu.movieapi.model.Movie;
import com.renu.movieapi.rest.dto.CreateMovieRequest;
import com.renu.movieapi.rest.dto.MovieDto;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class MovieMapperImpl implements MovieMapper {

    @Override
    public Movie toMovie(CreateMovieRequest createMovieRequest) {
        if (createMovieRequest == null) {
            return null;
        }
        return new Movie(createMovieRequest.getId(), createMovieRequest.getTitle(), null);
    }

    @Override
    public MovieDto toMovieDto(Movie movie) {
        if (movie == null) {
            return null;
        }
        return new MovieDto(movie.getId(), movie.getTitle(), movie.getImageURL(), DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(movie.getCreatedAt()));
    }
}
