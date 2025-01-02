package com.cinema_ticket.requests;

import com.cinema_ticket.entities.AuditoriumStatus;
import com.cinema_ticket.entities.Seat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AuditoriumRequest {
    private String name;
    private List<Seat> seats;
    private AuditoriumStatus status;
    private Integer seat_capacity;
    private Date created_at;
    private Date update_at;
}
