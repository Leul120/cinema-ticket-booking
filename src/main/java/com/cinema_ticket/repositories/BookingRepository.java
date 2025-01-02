package com.cinema_ticket.repositories;

import com.cinema_ticket.entities.Booking;
import com.cinema_ticket.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByUser(Long userId);
}
