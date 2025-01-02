package com.cinema_ticket.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user;
    @ManyToOne
    private Showtime showtime;
    @OneToMany
    private List<Seat> seats;
    @ManyToOne
    private Auditorium auditorium;
    private BookingStatus status;
    private Long total_amount;
    @Enumerated(EnumType.STRING)
    private Payment_status payment_status;
    private Date created_at;
    private Date updated_at;


}
