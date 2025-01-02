package com.cinema_ticket.services.Impl;

import com.cinema_ticket.entities.Auditorium;
import com.cinema_ticket.entities.Seat;
import com.cinema_ticket.entities.Status;
import com.cinema_ticket.repositories.AuditoriumRepository;
import com.cinema_ticket.repositories.SeatRepository;
import com.cinema_ticket.requests.AuditoriumRequest;
import com.cinema_ticket.services.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class AuditoriumServiceImpl implements AuditoriumService {
    private final SeatRepository seatRepository;
    private final AuditoriumRepository auditoriumRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    public AuditoriumServiceImpl(SeatRepository seatRepository, AuditoriumRepository auditoriumRepository){
        this.seatRepository=seatRepository;
        this.auditoriumRepository=auditoriumRepository;
    }
    @Override
    @Transactional
    public Auditorium addAuditorium(AuditoriumRequest auditoriumRequest) {
        System.out.println(auditoriumRequest);
        try {
            Auditorium auditorium=new Auditorium();
            auditorium.setName(auditoriumRequest.getName());
            List<Seat> seats=new ArrayList<>();
            for(Seat seat:auditoriumRequest.getSeats()){
                Seat seat1=new Seat();
                seat1.setName(seat.getName());
                seat1.setType(seat.getType());
//                seat1.setSeatStatus();
                Seat savedSeat=seatRepository.save(seat1);
                seats.add(savedSeat);
            }
            auditorium.setSeats(seats);
            auditorium.setStatus(auditoriumRequest.getStatus());
            auditorium.setSeat_capacity(auditoriumRequest.getSeat_capacity());
            auditorium.setCreated_at(new Date());
            auditorium.setUpdate_at(new Date());
            auditorium=auditoriumRepository.save(auditorium);
            messagingTemplate.convertAndSend("/topic/update-auditorium", "update");
            return auditorium;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Auditorium updateAuditorium(Long id, AuditoriumRequest auditoriumRequest) {
        // Fetch and update auditorium
        try {
            Auditorium auditorium = auditoriumRepository.findById(id)
                    .map(existingAuditorium -> updateExistingAuditorium(existingAuditorium, auditoriumRequest))
                    .orElseThrow(() -> new RuntimeException("Auditorium not found!"));

            // Save updated entity
            auditorium = auditoriumRepository.save(auditorium);

            // Send message after successful update
            messagingTemplate.convertAndSend("/topic/update-auditorium", "update");

            return auditorium;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Auditorium updateExistingAuditorium(Auditorium auditorium, AuditoriumRequest auditoriumRequest) {
        System.out.println(auditoriumRequest.getStatus());
        auditorium.setName(auditoriumRequest.getName());
        auditorium.setStatus(auditoriumRequest.getStatus());
        auditorium.setSeat_capacity(auditoriumRequest.getSeat_capacity()); // Fix this
        // Uncomment and handle seats if necessary
//         auditorium.setSeats(auditoriumRequest.getSeats());
        auditorium.setUpdate_at(new Date());
        return auditorium;
    }


    @Override
    public List<Auditorium> getAllAuditorium() {
        try {
            return auditoriumRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Auditorium getAuditoriumDetails(Long id) {
        try {
            return auditoriumRepository.findById(id).orElseThrow(()->new RuntimeException("Auditorium not found!"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void deleteAuditorium(Long id){
        try {
            auditoriumRepository.deleteById(id);
            messagingTemplate.convertAndSend("/topic/update-auditorium", "update");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
