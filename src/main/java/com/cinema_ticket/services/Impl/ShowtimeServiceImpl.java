package com.cinema_ticket.services.Impl;

import com.cinema_ticket.entities.*;
import com.cinema_ticket.repositories.AuditoriumRepository;
import com.cinema_ticket.repositories.SeatRepository;
import com.cinema_ticket.repositories.ShowtimeRepository;
import com.cinema_ticket.requests.SeatStatusRequest;
import com.cinema_ticket.requests.ShowtimeRequest;
import com.cinema_ticket.services.SeatService;
import com.cinema_ticket.services.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final AuditoriumRepository auditoriumRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final SeatService seatService;
    @Autowired
    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository,SeatRepository seatRepository,AuditoriumRepository auditoriumRepository,SeatService seatService){
        this.showtimeRepository=showtimeRepository;
        this.seatRepository=seatRepository;
        this.auditoriumRepository=auditoriumRepository;
        this.seatService=seatService;
    }
    public boolean isAuditoriumAvailable(Auditorium auditorium, LocalTime startTime, LocalTime endTime, LocalDate date){
        return showtimeRepository.findConflictingShowtimes(auditorium.getId(),date,startTime,endTime).isEmpty();
    }
    @Override
    @Transactional
    public Showtime addShowtime(ShowtimeRequest showtimeRequest) {
        try {

            if (showtimeRequest.getMovie() == null) {
                throw new IllegalArgumentException("Movie is required to create a showtime");
            }
            LocalTime endTime = showtimeRequest.getStart_time().plusMinutes(showtimeRequest.getMovie().getDuration());
            for(Auditorium auditorium:showtimeRequest.getAuditoriums()){
                if (!isAuditoriumAvailable(auditorium, showtimeRequest.getStart_time(), endTime,showtimeRequest.getDate())) {
                    throw new RuntimeException("Auditorium " + auditorium.getName() + " is not available for the selected time slot");
                }
            }
            Showtime showtime = new Showtime();
            showtime.setMovie(showtimeRequest.getMovie());
            showtime.setAuditoriums(showtimeRequest.getAuditoriums()); // Set auditoriums from request
            showtime.setStart_time(showtimeRequest.getStart_time());

            // Calculate the end time by adding movie duration (in minutes) to the start time
            showtime.setEnd_time(endTime);

            showtime.setDate(showtimeRequest.getDate());
            showtime.setPrice(showtimeRequest.getPrice());
            System.out.println(showtimeRequest.getMovie().getDuration());  // Debugging

            showtime.setCreated_at(new Date());
            SeatStatusRequest seatStatusRequest=new SeatStatusRequest();
            seatStatusRequest.setStatus(Status.AVAILABLE);
            seatStatusRequest.setTime(showtimeRequest.getDate().atTime(showtimeRequest.getStart_time()));
            for(Auditorium auditorium:showtimeRequest.getAuditoriums()){
                for(Seat seat:auditorium.getSeats()){
                    seatService.addSeatStatus(seat.getId(),seatStatusRequest);
                }
            }
            showtime=showtimeRepository.save(showtime);
            messagingTemplate.convertAndSend("/topic/update-showtime", "update");
            return showtime;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @Transactional
    public Showtime updateShowtime(Long id, ShowtimeRequest showtimeRequest) {
        Showtime showtime=showtimeRepository.findById(id).map(existingShowtime->updateExistingShowtime(existingShowtime,showtimeRequest)).map(showtimeRepository::save).orElseThrow(()->new RuntimeException("Showtime not found!"));
        messagingTemplate.convertAndSend("/topic/update-showtime", "update");
        return showtime;
    }
    private Showtime updateExistingShowtime(Showtime showtime, ShowtimeRequest showtimeRequest) {
        showtime.setMovie(showtimeRequest.getMovie());
        showtime.setAuditoriums(showtimeRequest.getAuditoriums()); // Set auditoriums from request
        showtime.setStart_time(showtimeRequest.getStart_time());

        // Calculate the end time by adding movie duration (in minutes) to the start time
        LocalTime endTime = showtimeRequest.getStart_time().plusMinutes(showtimeRequest.getMovie().getDuration());
        showtime.setEnd_time(endTime);

        showtime.setDate(showtimeRequest.getDate());
        showtime.setPrice(showtimeRequest.getPrice());
        System.out.println(showtimeRequest.getMovie().getDuration());  // Debugging

        showtime.setCreated_at(new Date());
        return showtime;
    }


    @Override
    public void deleteShowtime(Long id) {
        try {

            Showtime showtime=showtimeRepository.findById(id).orElseThrow(()->new RuntimeException("Showtime not found!"));
            for(Auditorium auditorium:showtime.getAuditoriums()){
                for(Seat seat:auditorium.getSeats()){
                    SeatStatus seatStatus=seat.getSeatStatuses().stream().filter(s->s.getTime().equals(showtime.getDate().atTime(showtime.getStart_time()))).findFirst().orElseThrow(()->new RuntimeException("Seat Status not found!"));
                    seatService.deleteSeatStatus(seat.getId(),seatStatus.getId());
                }
            }
            showtimeRepository.deleteById(id);
            messagingTemplate.convertAndSend("/topic/update-showtime", "update");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Showtime> getShowtime() {
        return showtimeRepository.findAll();
    }
    @Override
    public Showtime getShowtimeDetails(Long id){
        return showtimeRepository.findById(id).orElseThrow(()->new RuntimeException("Showtime not found!"));
    }

    @Override
    public List<Seat> getAvailableSeats(Long id) {
        Showtime showtime=showtimeRepository.findById(id).orElseThrow(()->new RuntimeException("Showtime not found!"));
        List<Seat> seatsList=new ArrayList<>();
//        for(Auditorium auditorium:showtime.getAuditoriums()) {
//            List<Seat> seats = auditorium.getSeats().stream().filter(seat->seat.getStatus()==Status.AVAILABLE).collect(Collectors.toList());
//            seatsList.addAll(seats);
//        }
        return seatsList;
    }
}
