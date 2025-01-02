package com.cinema_ticket.requests;

import lombok.Data;
import lombok.Getter;

@Data
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

//    public String getEmail(){
//        return email;
//    }

}
