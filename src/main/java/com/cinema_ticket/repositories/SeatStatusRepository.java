package com.cinema_ticket.repositories;

import com.cinema_ticket.entities.Seat;
import com.cinema_ticket.entities.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SeatStatusRepository extends JpaRepository<SeatStatus,Long> {
    Optional<SeatStatus> findByTimeAndSeat(LocalDateTime time, Seat seat);
}
