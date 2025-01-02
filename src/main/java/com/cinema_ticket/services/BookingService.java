package com.cinema_ticket.services;

import com.cinema_ticket.entities.Booking;
import com.cinema_ticket.entities.User;
import com.cinema_ticket.requests.BookingRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface BookingService {
    Booking createBooking(Long userId, BookingRequest bookingRequest);
    Booking confirmBooking(Long id);
    Booking cancelBooking(Long id);
    List<Booking> getUserBookings(Long userId);
    Booking getBookingDetails(Long id);
    List<Booking> getAllBookings();
    void deleteBooking(Long id);
}
