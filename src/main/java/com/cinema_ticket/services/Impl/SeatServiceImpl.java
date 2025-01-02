package com.cinema_ticket.services.Impl;

import com.cinema_ticket.config.Patcher;
import com.cinema_ticket.entities.Seat;
import com.cinema_ticket.entities.SeatStatus;
import com.cinema_ticket.repositories.SeatRepository;
import com.cinema_ticket.repositories.SeatStatusRepository;
import com.cinema_ticket.requests.SeatRequest;
import com.cinema_ticket.requests.SeatStatusRequest;
import com.cinema_ticket.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final SeatStatusRepository seatStatusRepository;
    @Autowired
    private static Patcher patcher;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    public SeatServiceImpl(SeatRepository seatRepository,Patcher patcher,SeatStatusRepository seatStatusRepository){
        this.seatRepository=seatRepository;
//        this.patcher=patcher;
        this.seatStatusRepository=seatStatusRepository;
    }
    @Override
    @Transactional
    public Seat updateSeat(Long id, SeatRequest seatRequest)  {
        try {
            System.out.println(seatRequest);
            Seat existingSeat= seatRepository.findById(id).orElseThrow(()->new RuntimeException("seat not found!"));
            Seat seat=new Seat();
            seat.setName(seatRequest.getName());
            seat.setType(seatRequest.getType());
            patcher.seatPatcher(existingSeat,seat);
            seat=seatRepository.save(existingSeat);
            messagingTemplate.convertAndSend("/topic/update-auditorium", "update");
            return seat;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }
    @Override
    @Transactional
    public Seat addSeatStatus(Long id, SeatStatusRequest seatStatusRequest) {
        try {
            Seat existingSeat = seatRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("seat not found!"));

            SeatStatus seatStatus = new SeatStatus();
            // Set both sides of the relationship
            seatStatus.setSeat(existingSeat);

            seatStatus.setTime(seatStatusRequest.getTime());
            seatStatus.setStatus(seatStatusRequest.getStatus());
            seatStatus.setPrice(seatStatusRequest.getPrice());

            // Initialize the set if null
            if (existingSeat.getSeatStatuses() == null) {
                existingSeat.setSeatStatuses(new ArrayList<>());
            }

            // Add to both sides of the relationship
            existingSeat.getSeatStatuses().add(seatStatus);

            // Save the seat which will cascade to seat status due to the relationship
            existingSeat=seatRepository.save(existingSeat);
            messagingTemplate.convertAndSend("/topic/update-auditorium", "update");
            return existingSeat;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @Override
    @Transactional
    public Seat deleteSeatStatus(Long id, Long seatStatusId) {
        try {
            Seat existingSeat = seatRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("seat not found!"));

           SeatStatus seatStatus=existingSeat.getSeatStatuses().stream().filter(s->s.getId().equals(seatStatusId)).findFirst().orElseThrow(()->new RuntimeException("Seat status not found!"));
            existingSeat.getSeatStatuses().remove(seatStatus);
            existingSeat=seatRepository.save(existingSeat);
            messagingTemplate.convertAndSend("/topic/update-auditorium", "update");
            return existingSeat;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public SeatStatus updateSeatStatus(Long id, SeatStatusRequest seatStatusRequest) {
        try {
            SeatStatus existingSeatStatus = seatStatusRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("seat not found!"));
            SeatStatus seatStatus=new SeatStatus();
            seatStatus.setTime(seatStatusRequest.getTime());
            seatStatus.setStatus(seatStatusRequest.getStatus());
            seatStatus.setPrice(seatStatusRequest.getPrice());
            patcher.seatStatusPatcher(existingSeatStatus,seatStatus);
            existingSeatStatus=seatStatusRepository.save(existingSeatStatus);
            messagingTemplate.convertAndSend("/topic/update-auditorium", "update");
            return existingSeatStatus;
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteSeat(Long id) {
        try {
            seatRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Seat> getAllSeats(){
        return seatRepository.findAll();
    }
}
