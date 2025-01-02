package com.cinema_ticket.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Auditorium implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Seat> seats;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AuditoriumStatus status;
    private Integer seat_capacity;
    @ManyToMany(mappedBy = "auditoriums")
    @JsonIgnoreProperties("auditoriums")
    private Set<Showtime> showtimes = new HashSet<>();
    private Date created_at;
    private Date update_at;
}
