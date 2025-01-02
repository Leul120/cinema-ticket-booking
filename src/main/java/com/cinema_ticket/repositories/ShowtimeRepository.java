package com.cinema_ticket.repositories;

import com.cinema_ticket.entities.Seat;
import com.cinema_ticket.entities.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Query("SELECT s FROM Showtime s " +
            "JOIN s.auditoriums a " +
            "WHERE a.id = :auditoriumId " +
            "AND s.date = :date " +
            "AND ((s.start_time <= :startTime AND s.end_time > :startTime) " +
            "OR (s.start_time < :endTime AND s.end_time >= :endTime) " +
            "OR (s.start_time >= :startTime AND s.end_time <= :endTime))")
    List<Showtime> findConflictingShowtimes(
            @Param("auditoriumId") Long auditoriumId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}