package com.cinema_ticket.services;

import com.cinema_ticket.entities.Auditorium;
import com.cinema_ticket.requests.AuditoriumRequest;

import java.util.List;

public interface AuditoriumService {
    Auditorium addAuditorium(AuditoriumRequest auditoriumRequest);
    Auditorium updateAuditorium(Long id,AuditoriumRequest auditoriumRequest);
    List<Auditorium> getAllAuditorium();
    Auditorium getAuditoriumDetails(Long id);
    void deleteAuditorium(Long id);
}
