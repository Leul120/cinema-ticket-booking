package com.cinema_ticket.services;

import com.cinema_ticket.entities.Seat;
import com.cinema_ticket.entities.SeatStatus;
import com.cinema_ticket.requests.SeatRequest;
import com.cinema_ticket.requests.SeatStatusRequest;

import java.util.List;

public interface SeatService {
 Seat updateSeat(Long id, SeatRequest seatRequest);
 void deleteSeat(Long id);
 List<Seat> getAllSeats();
 Seat addSeatStatus(Long id, SeatStatusRequest seatStatusRequest);
 SeatStatus updateSeatStatus(Long id, SeatStatusRequest seatStatusRequest);
 Seat deleteSeatStatus(Long id, Long seatStatusId);
}
