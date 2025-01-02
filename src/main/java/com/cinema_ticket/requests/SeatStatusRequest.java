package com.cinema_ticket.requests;

import com.cinema_ticket.entities.Seat;
import com.cinema_ticket.entities.Status;
import com.cinema_ticket.entities.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeatStatusRequest {
    private Seat seat;
    private Status status;
    private LocalDateTime time;
    private Long user;
    private Long price;
}
