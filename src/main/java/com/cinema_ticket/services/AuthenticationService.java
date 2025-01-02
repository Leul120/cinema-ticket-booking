package com.cinema_ticket.services;


import com.cinema_ticket.requests.JwtAuthenticationRequest;
import com.cinema_ticket.requests.RefreshTokenRequest;
import com.cinema_ticket.requests.SignInRequest;
import com.cinema_ticket.requests.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationRequest signup(SignUpRequest signUpRequest);
    JwtAuthenticationRequest signIn(SignInRequest signInRequest);
    JwtAuthenticationRequest refreshToken(RefreshTokenRequest refreshTokenRequest);
}
