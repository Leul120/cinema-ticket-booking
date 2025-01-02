package com.cinema_ticket.services;

import com.cinema_ticket.entities.Movie;
import com.cinema_ticket.requests.MovieRequest;

import java.util.List;

public interface MovieService {
    Movie addMovie(MovieRequest movieRequest);
    Movie updateMovie(Long id,MovieRequest movieRequest);
    void deleteMovie(Long id);
    List<Movie> getAllMovies();
    Movie getMovieDetails(Long id);

}
