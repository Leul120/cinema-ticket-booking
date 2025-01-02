package com.cinema_ticket.repositories;

import com.cinema_ticket.entities.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriumRepository extends JpaRepository<Auditorium,Long> {

}
