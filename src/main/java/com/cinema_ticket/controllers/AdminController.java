package com.cinema_ticket.controllers;

import com.cinema_ticket.entities.SeatStatus;
import com.cinema_ticket.entities.User;
import com.cinema_ticket.repositories.UserRepository;
import com.cinema_ticket.requests.*;
import com.cinema_ticket.responses.ApiResponse;
import com.cinema_ticket.services.*;
import com.yaphet.chapa.Chapa;
import com.yaphet.chapa.model.PostData;
import jakarta.websocket.OnMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final MovieService movieService;
    private final ShowtimeService showtimeService;
    private final AuditoriumService auditoriumService;
    private final SeatService seatService;
    private final BookingService bookingService;
    private final Chapa chapa;
    private final ImageService imageService;
    private final ReviewService reviewService;
    private final UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    public AdminController(MovieService movieService,ShowtimeService showtimeService,AuditoriumService auditoriumService,SeatService seatService,BookingService bookingService,Chapa chapa,ReviewService reviewService,ImageService imageService,UserService userService){
        this.movieService=movieService;
        this.showtimeService=showtimeService;
        this.auditoriumService=auditoriumService;
        this.seatService=seatService;
        this.bookingService=bookingService;
        this.chapa=chapa;
        this.reviewService=reviewService;
        this.imageService=imageService;
        this.userService=userService;
    }
    @PostMapping("/add-movie")
    public ResponseEntity<ApiResponse> addMovie(@RequestBody MovieRequest movieRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",movieService.addMovie(movieRequest)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-movie/{id}")
    public ResponseEntity<ApiResponse> getMovie(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("success",movieService.getMovieDetails(id)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-all-movies")
    public ResponseEntity<ApiResponse> getAllMovies(){
        try {
            return ResponseEntity.ok(new ApiResponse("success",movieService.getAllMovies()));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @DeleteMapping("/delete-movie/{id}")
    public ResponseEntity<ApiResponse> deleteMovie(@PathVariable Long id){
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.ok(new ApiResponse("success","Deleted successfully!"));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-user-bookings")
    public ResponseEntity<ApiResponse> getUserBookings(@RequestAttribute("authenticatedUser") Long userId){
        try {
            return ResponseEntity.ok(new ApiResponse("success",bookingService.getUserBookings(userId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PutMapping("/update-movie/{id}")
    public ResponseEntity<ApiResponse> updateMovie(@PathVariable Long id,@RequestBody MovieRequest movieRequest){
        try {

            return ResponseEntity.ok(new ApiResponse("success",movieService.updateMovie(id,movieRequest)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/add-showtime")
    public ResponseEntity<ApiResponse> addShowtime(@RequestBody ShowtimeRequest showtimeRequest){
        try {

            return ResponseEntity.ok(new ApiResponse("success",showtimeService.addShowtime(showtimeRequest)));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PutMapping("/update-showtime/{id}")
    public ResponseEntity<ApiResponse> updateShowtime(@PathVariable Long id,@RequestBody ShowtimeRequest showtimeRequest){
        try {

            return ResponseEntity.ok(new ApiResponse("success",showtimeService.updateShowtime(id,showtimeRequest)));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @DeleteMapping("/delete-showtime/{id}")
    public ResponseEntity<ApiResponse> deleteShowtime(@PathVariable Long id){
        try {
            showtimeService.deleteShowtime(id);
            return ResponseEntity.ok(new ApiResponse("success","Deleted successfully!"));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @DeleteMapping("/delete-seat/{id}")
    public ResponseEntity<ApiResponse> deleteSeat(@PathVariable Long id){
        try {
            seatService.deleteSeat(id);
            return ResponseEntity.ok(new ApiResponse("success","Deleted successfully!"));
        } catch (Exception e) {

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
    @GetMapping("/get-available-seats/{id}")
    public ResponseEntity<ApiResponse> getAvailableSeats(@PathVariable Long id){
        try {

            return ResponseEntity.ok(new ApiResponse("success",showtimeService.getAvailableSeats(id)));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/add-auditorium")
    public ResponseEntity<ApiResponse> addAuditorium(@RequestBody AuditoriumRequest auditoriumRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",auditoriumService.addAuditorium(auditoriumRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PutMapping("/update-auditorium/{id}")
    public ResponseEntity<ApiResponse> updateAuditorium(@PathVariable Long id, @RequestBody AuditoriumRequest auditoriumRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",auditoriumService.updateAuditorium(id,auditoriumRequest)));
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
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");
    }

    @PatchMapping("/update-seat/{id}")
    public ResponseEntity<ApiResponse> updateAuditorium(@PathVariable Long id, @RequestBody SeatRequest seatRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",seatService.updateSeat(id,seatRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-all-auditorium")
    public ResponseEntity<ApiResponse> getAllAuditorium(){
        try {
            return ResponseEntity.ok(new ApiResponse("success",auditoriumService.getAllAuditorium()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-all-user")
    public ResponseEntity<ApiResponse> getAllUser(){
        try {
            return ResponseEntity.ok(new ApiResponse("success",userService.getAllUser()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-auditorium-details/{id}")
    public ResponseEntity<ApiResponse> getAuditoriumDetails(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("success",auditoriumService.getAuditoriumDetails(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @DeleteMapping("/delete-auditorium/{id}")
    public ResponseEntity<ApiResponse> deleteAuditorium(@PathVariable Long id){
        try {
            auditoriumService.deleteAuditorium(id);
            return ResponseEntity.ok(new ApiResponse("success","Deleted successfully!"));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-all-seats")
    public ResponseEntity<ApiResponse> getAllSeats(){
        try {

            return ResponseEntity.ok(new ApiResponse("success",seatService.getAllSeats()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/create-booking")
    public ResponseEntity<ApiResponse> createBooking(@RequestAttribute("authenticatedUser") Long userId, @RequestBody BookingRequest bookingRequest){
        System.out.println(userId);
        try {
            return ResponseEntity.ok(new ApiResponse("success",bookingService.createBooking(userId,bookingRequest)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PatchMapping("/add-seat-status/{id}")
    public ResponseEntity<ApiResponse> addSeatStatus(@PathVariable Long id, @RequestBody SeatStatusRequest seatStatusRequest){
//        System.out.println(seatStatusRequest);
        try {
            return ResponseEntity.ok(new ApiResponse("success",seatService.addSeatStatus(id,seatStatusRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PatchMapping("/update-seat-status/{id}")
    public ResponseEntity<ApiResponse> updateSeatStatus(@PathVariable Long id, @RequestBody SeatStatusRequest seatStatusRequest){
//        System.out.println(seatStatusRequest);
        try {
            return ResponseEntity.ok(new ApiResponse("success",seatService.updateSeatStatus(id,seatStatusRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @DeleteMapping("/delete-seat-status/{id}/{seatStatusId}")
    public ResponseEntity<ApiResponse> deleteSeatStatus(@PathVariable Long id, @PathVariable Long seatStatusId){
//        System.out.println(seatStatusRequest);
        try {
            return ResponseEntity.ok(new ApiResponse("success",seatService.deleteSeatStatus(id,seatStatusId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/initialize-payment")
    public ResponseEntity<ApiResponse> initializePayment(@RequestBody  PostData postData){

        try {


            return ResponseEntity.ok(new ApiResponse("success",chapa.initialize(postData) ));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/verify-payment/{id}/{transaction}")
    public ResponseEntity<ApiResponse> initializePayment(@PathVariable  String transaction,@PathVariable Long id){
        try {
            chapa.verify(transaction);
            System.out.println("verify");
            return ResponseEntity.ok(new ApiResponse("success",bookingService.confirmBooking(id)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (Throwable e) {
            throw new RuntimeException(e);
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
    @GetMapping("/cancel-booking/{id}")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("success",bookingService.cancelBooking(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/get-all-bookings")
    public ResponseEntity<ApiResponse> getAllBookings(){
        try {
            return ResponseEntity.ok(new ApiResponse("success",bookingService.getAllBookings()));
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
    @PostMapping("/create-review/{id}")
    public ResponseEntity<ApiResponse> createReview(@RequestAttribute("user") User user,@PathVariable Long id,@RequestBody ReviewRequest reviewRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",reviewService.createReview(user,id,reviewRequest)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse> createReview(@RequestBody ImageRequest imageRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",imageService.uploadImage(imageRequest)));
        } catch (Exception e) {
            System.out.println("hello");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
