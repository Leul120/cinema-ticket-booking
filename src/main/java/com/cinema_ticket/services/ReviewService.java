package com.cinema_ticket.services;

import com.cinema_ticket.entities.Review;
import com.cinema_ticket.entities.User;
import com.cinema_ticket.requests.ReviewRequest;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    Review createReview(User user,Long id, ReviewRequest reviewRequest);
    Review updateReview(UUID id, ReviewRequest reviewRequest);
    void deleteReview(UUID id);
    Review getReview(UUID id);
    List<Review> getAllReview();

}
