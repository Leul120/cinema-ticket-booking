package com.cinema_ticket.requests;

import com.cinema_ticket.entities.Auditorium;
import com.cinema_ticket.entities.Movie;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class ShowtimeRequest {

    private Movie movie;
    private Set<Auditorium> auditoriums;
    private LocalTime start_time;
    private LocalDate date;
    private LocalTime end_time;
    private Long price;
    private Date created_at;
}
