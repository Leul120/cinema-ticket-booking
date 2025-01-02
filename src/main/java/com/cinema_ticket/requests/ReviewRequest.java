package com.cinema_ticket.requests;

import com.cinema_ticket.entities.Movie;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ReviewRequest {
    private Long rating;
    private String comment;
    private Movie movie;
}
