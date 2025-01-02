package com.cinema_ticket.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private User user;
//    private String message;
//    private Boolean is_read;
//    private String type;
//    private Date created_at;
}
