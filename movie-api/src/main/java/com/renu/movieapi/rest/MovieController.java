package com.renu.movieapi.rest;

import com.renu.movieapi.mapper.MovieMapper;
import com.renu.movieapi.model.Movie;
import com.renu.movieapi.model.MovieImageRequest;
import com.renu.movieapi.rest.dto.CreateMovieRequest;
import com.renu.movieapi.rest.dto.MovieDto;
import com.renu.movieapi.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @GetMapping
    public List<MovieDto> getMovies(@RequestParam(value = "text", required = false) String text) {
        List<Movie> movies = movieService.getMovies();
        return movies.stream()
                .map(movieMapper::toMovieDto)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data")
    public MovieDto createMovie(@ModelAttribute CreateMovieRequest createMovieRequest) throws IOException {
        // Create a new Movie object from the request
        Movie movie = new Movie();
        movie.setTitle(createMovieRequest.getTitle());
        movie.setImageURL(null); // Initially, no image URL

        // Save the movie and upload the image
        return movieMapper.toMovieDto(movieService.saveMovie(movie, createMovieRequest.getFile()));
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Void> updateMovieImage(@PathVariable("id") Long id, @RequestBody MovieImageRequest movieImageRequest){
        movieService.updateMovieImage(id, movieImageRequest);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/testapi")
    public String testApi(){
        return "Test API in Movies service ";
    }
}
