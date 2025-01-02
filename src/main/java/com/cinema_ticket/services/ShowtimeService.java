package com.cinema_ticket.services;

import com.cinema_ticket.entities.Seat;
import com.cinema_ticket.entities.Showtime;
import com.cinema_ticket.requests.ShowtimeRequest;

import java.util.List;

public interface ShowtimeService {
    Showtime addShowtime(ShowtimeRequest showtimeRequest);
    Showtime updateShowtime(Long id,ShowtimeRequest showtimeRequest);
    void deleteShowtime(Long id);
    List<Showtime> getShowtime();
    Showtime getShowtimeDetails(Long id);
    List<Seat> getAvailableSeats(Long id);
}
