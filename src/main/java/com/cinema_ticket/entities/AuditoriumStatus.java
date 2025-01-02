package com.cinema_ticket.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AuditoriumStatus {
    ACTIVE,
    INACTIVE,
    MAINTENANCE,

}
