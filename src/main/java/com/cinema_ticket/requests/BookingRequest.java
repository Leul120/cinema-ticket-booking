package com.cinema_ticket.requests;

import com.cinema_ticket.entities.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class BookingRequest {
    private Showtime showtime;
    private List<Seat> seats;
    private Long total_amount;
    private Auditorium auditorium;
    private Payment_status payment_status;
    private Date created_at;
    private Date updated_at;
}
