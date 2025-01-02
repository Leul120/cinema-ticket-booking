package com.cinema_ticket.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class Promocode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Long discount_value;
    private Date valid_from;
    private Date valid_to;
    private Integer usage_limit;
    private Integer used_count;
}
