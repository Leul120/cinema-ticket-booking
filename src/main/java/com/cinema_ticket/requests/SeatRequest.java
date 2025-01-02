package com.cinema_ticket.requests;

import com.cinema_ticket.entities.SeatStatus;
import com.cinema_ticket.entities.SeatType;
import com.cinema_ticket.entities.Status;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SeatRequest {
    private String name;
    private SeatType type;
    private List<SeatStatus> seatStatuses;
    private Long price;
}
