package com.cinema_ticket.services.Impl;

import com.cinema_ticket.config.Patcher;
import com.cinema_ticket.entities.Movie;
import com.cinema_ticket.entities.Review;
import com.cinema_ticket.entities.User;
import com.cinema_ticket.repositories.MovieRepository;
import com.cinema_ticket.repositories.ReviewRepository;
import com.cinema_ticket.requests.ReviewRequest;
import com.cinema_ticket.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,MovieRepository movieRepository){
        this.reviewRepository=reviewRepository;
        this.movieRepository=movieRepository;
    }
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    Patcher patcher;
    @Override
    @Transactional
    public Review createReview(User user,Long id,ReviewRequest reviewRequest) {
        try {
            Movie movie=movieRepository.findById(id).orElseThrow(()->new RuntimeException("Movie not found!"));
            Review review=new Review();
            review.setRating(reviewRequest.getRating());
            review.setComment(reviewRequest.getComment());
            review.setMovie(movie);
            review.setUser(user);
            movie.getReviews().add(review);
            movieRepository.save(movie);
            messagingTemplate.convertAndSend("/topic/update-movie", "update");
            return review;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Review updateReview(UUID id, ReviewRequest reviewRequest) {
        try {
            Review existingReview=reviewRepository.findById(id).orElseThrow(()->new RuntimeException("Review not found!"));
            Review review=new Review();
            review.setRating(reviewRequest.getRating());
            review.setComment(reviewRequest.getComment());
            review.setMovie(reviewRequest.getMovie());
            patcher.reviewPatcher(existingReview,review);
            existingReview=reviewRepository.save(existingReview);
            messagingTemplate.convertAndSend("/topic/update-movie", "update");
            return existingReview;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional
    public void deleteReview(UUID id) {
        try {
            reviewRepository.deleteById(id);
            messagingTemplate.convertAndSend("/topic/update-movie", "update");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Review getReview(UUID id) {
        return reviewRepository.findById(id).orElseThrow(()->new RuntimeException("Review not found!"));
    }
    @Override
    public List<Review> getAllReview() {
        return reviewRepository.findAll();
    }
}
