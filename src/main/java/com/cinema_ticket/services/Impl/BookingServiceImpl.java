package com.cinema_ticket.services.Impl;

import com.cinema_ticket.config.Patcher;
import com.cinema_ticket.entities.*;
import com.cinema_ticket.repositories.BookingRepository;
import com.cinema_ticket.repositories.SeatRepository;
import com.cinema_ticket.repositories.SeatStatusRepository;
import com.cinema_ticket.repositories.UserRepository;
import com.cinema_ticket.requests.BookingRequest;
import com.cinema_ticket.requests.SeatStatusRequest;
import com.cinema_ticket.services.BookingService;
import com.cinema_ticket.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.awt.print.Book;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final SeatStatusRepository seatStatusRepository;
    private final SeatService seatService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    public BookingServiceImpl(UserRepository userRepository,BookingRepository bookingRepository,SeatRepository seatRepository,SeatStatusRepository seatStatusRepository,SeatService seatService){
        this.bookingRepository=bookingRepository;
        this.userRepository=userRepository;
        this.seatRepository=seatRepository;
        this.seatStatusRepository=seatStatusRepository;
        this.seatService=seatService;
    }
    @Autowired
    Patcher patcher;
    @Override
    @Transactional
    public Booking createBooking(Long userId, BookingRequest bookingRequest) {
        System.out.println("Auditorium ID: " + bookingRequest.getAuditorium());
        System.out.println("Total Amount: " + bookingRequest.getTotal_amount());
        System.out.println("Status: " + BookingStatus.PENDING);
        try {

            System.out.println(userId);
            Booking booking = new Booking();
            booking.setUser(userId);
            booking.setShowtime(bookingRequest.getShowtime());
            booking.setAuditorium(bookingRequest.getAuditorium());
            booking.setTotal_amount(bookingRequest.getTotal_amount());
            booking.setStatus(BookingStatus.PENDING);
            booking.setCreated_at(new Date());
            booking.setUpdated_at(new Date());

            booking.setSeats(bookingRequest.getSeats());
            for (Seat seat : bookingRequest.getSeats()) {
                SeatStatus seatStatus = seatStatusRepository.findByTimeAndSeat(bookingRequest.getShowtime().getDate()
                        .atTime(bookingRequest.getShowtime().getStart_time()),seat).orElseThrow(()->new RuntimeException("Seat status not found!"));
                System.out.println(seatStatus);
                SeatStatusRequest seatStatusRequest=new SeatStatusRequest();
                seatStatusRequest.setStatus(Status.BOOKED);
                seatStatusRequest.setUser(userId);
                seatService.updateSeatStatus(seatStatus.getId(),seatStatusRequest);
            }
            booking = bookingRepository.save(booking);
            messagingTemplate.convertAndSend("/topic/update-booking", "update");
            return booking;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public Booking confirmBooking(Long id) {
        System.out.println("confirm");
        try {
            Booking booking=bookingRepository.findById(id).orElseThrow(()->new RuntimeException("Booking not found!"));
            booking.setPayment_status(Payment_status.paid);
            booking.setStatus(BookingStatus.CONFIRMED);
            booking=bookingRepository.save(booking);
            messagingTemplate.convertAndSend("/topic/update-booking", "update");
            return booking;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public Booking cancelBooking(Long id) {
        try {
            Booking booking=bookingRepository.findById(id).orElseThrow(()->new RuntimeException("Booking not found!"));
            booking.setStatus(BookingStatus.CANCELLED);

            for(Seat seat:booking.getSeats()){
                SeatStatus seatStatus = seatStatusRepository.findByTimeAndSeat(booking.getShowtime().getDate()
                        .atTime(booking.getShowtime().getStart_time()),seat).orElseThrow(()->new RuntimeException("Seat status not found!"));
                SeatStatusRequest seatStatusRequest=new SeatStatusRequest();
                       seatStatusRequest.setStatus(Status.AVAILABLE);
                       seatStatusRequest.setUser(null);
                       seatService.updateSeatStatus(seatStatus.getId(),seatStatusRequest);
            }
            booking=bookingRepository.save(booking);
            messagingTemplate.convertAndSend("/topic/update-booking", "update");
            return booking;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> getUserBookings(Long userId) {
        try {
            return bookingRepository.findByUser(userId);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void deleteBooking(Long id) {
        try {
            Booking booking=bookingRepository.findById(id).orElseThrow(()->new RuntimeException("Booking not found!"));
            for(Seat seat:booking.getSeats()){
                SeatStatus seatStatus = seatStatusRepository.findByTimeAndSeat(booking.getShowtime().getDate()
                        .atTime(booking.getShowtime().getStart_time()),seat).orElseThrow(()->new RuntimeException("Seat status not found!"));
                SeatStatusRequest seatStatusRequest=new SeatStatusRequest();
                seatStatusRequest.setStatus(Status.AVAILABLE);
                seatStatusRequest.setUser(null);
                seatService.updateSeatStatus(seatStatus.getId(),seatStatusRequest);
            }
             bookingRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Booking> getAllBookings() {
        try {
            return bookingRepository.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Booking getBookingDetails(Long id) {
        try {
            return bookingRepository.findById(id).orElseThrow(()->new RuntimeException("Booking not found!"));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
