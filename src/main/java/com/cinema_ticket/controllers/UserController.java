package com.cinema_ticket.controllers;

import com.cinema_ticket.entities.User;
import com.cinema_ticket.requests.BookingRequest;
import com.cinema_ticket.requests.ReviewRequest;
import com.cinema_ticket.responses.ApiResponse;
import com.cinema_ticket.services.BookingService;
import com.cinema_ticket.services.MovieService;
import com.cinema_ticket.services.ReviewService;
import com.cinema_ticket.services.ShowtimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final BookingService bookingService;
    private final ShowtimeService showtimeService;
    private final MovieService movieService;
    private final ReviewService reviewService;
    public UserController(BookingService bookingService, ShowtimeService showtimeService,MovieService movieService,ReviewService reviewService){
        this.bookingService=bookingService;
        this.showtimeService=showtimeService;
        this.movieService=movieService;
        this.reviewService=reviewService;
    }

    @PostMapping("/create-booking")
    public ResponseEntity<ApiResponse> createBooking(@RequestAttribute("authenticatedUser") Long userId,@RequestBody BookingRequest bookingRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",bookingService.createBooking(userId,bookingRequest)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/create-review/{id}")
    public ResponseEntity<ApiResponse> createReview(@RequestAttribute("user") User user,@PathVariable Long id,@RequestBody ReviewRequest reviewRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",reviewService.createReview(user,id,reviewRequest)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PatchMapping("/update-review/{id}")
    public ResponseEntity<ApiResponse> updateReview(@PathVariable UUID id, @RequestBody ReviewRequest reviewRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",reviewService.updateReview(id,reviewRequest)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @DeleteMapping("/delete-review/{id}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable UUID id){
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(new ApiResponse("success","Review deleted successfully!"));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-review/{id}")
    public ResponseEntity<ApiResponse> getReview(@PathVariable UUID id){
        try {

            return ResponseEntity.ok(new ApiResponse("success",reviewService.getReview(id)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-all-reviews")
    public ResponseEntity<ApiResponse> getAllReviews(){
        try {

            return ResponseEntity.ok(new ApiResponse("success",reviewService.getAllReview()));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-showtime")
    public ResponseEntity<ApiResponse> getShowtime(){
        try {
            return ResponseEntity.ok(new ApiResponse("success",showtimeService.getShowtime()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-movie/{id}")
    public ResponseEntity<ApiResponse> getMovie(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("success",movieService.getMovieDetails(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-showtime/{id}")
    public ResponseEntity<ApiResponse> getShowtimeDetails(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("success",showtimeService.getShowtimeDetails(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-available-seats/{id}")
    public ResponseEntity<ApiResponse> getAvailableSeats(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("success",showtimeService.getAvailableSeats(id)));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PatchMapping("/confirm-booking/{id}")
    public ResponseEntity<ApiResponse> confirmBooking(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("success",bookingService.confirmBooking(id)));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PatchMapping("/cancel-booking/{id}")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("success",bookingService.cancelBooking(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-user-bookings/{id}")
    public ResponseEntity<ApiResponse> getUserBookings(@RequestAttribute("authenticatedUser") Long userId){
        try {
            return ResponseEntity.ok(new ApiResponse("success",bookingService.getUserBookings(userId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-booking-details/{id}")
    public ResponseEntity<ApiResponse> getBookingDetails(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("success",bookingService.getBookingDetails(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


}
