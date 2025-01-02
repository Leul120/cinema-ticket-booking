package com.cinema_ticket.repositories;

import com.cinema_ticket.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Long> {
}
