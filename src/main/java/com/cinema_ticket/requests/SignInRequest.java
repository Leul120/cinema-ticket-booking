package com.cinema_ticket.requests;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
