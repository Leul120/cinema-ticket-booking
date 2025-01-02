package com.cinema_ticket.repositories;

import com.cinema_ticket.entities.Auditorium;
import com.cinema_ticket.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {
//    List<Seat> findByAuditorium(Auditorium auditorium);
}
