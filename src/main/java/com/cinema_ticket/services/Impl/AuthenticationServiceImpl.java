package com.cinema_ticket.services.Impl;


import com.cinema_ticket.entities.Role;
import com.cinema_ticket.entities.User;
import com.cinema_ticket.repositories.UserRepository;
import com.cinema_ticket.requests.JwtAuthenticationRequest;
import com.cinema_ticket.requests.RefreshTokenRequest;
import com.cinema_ticket.requests.SignInRequest;
import com.cinema_ticket.requests.SignUpRequest;
import com.cinema_ticket.services.AuthenticationService;
import com.cinema_ticket.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager,JwtService jwtService){
        this.userRepository=userRepository;
        this.authenticationManager=authenticationManager;
        this.passwordEncoder=passwordEncoder;
        this.jwtService=jwtService;
    }
    @Override
    public JwtAuthenticationRequest signup(SignUpRequest signUpRequest) {
        Optional<User> oldUser=userRepository.findByEmail(signUpRequest.getEmail());
        System.out.println(oldUser);
        if(oldUser.isEmpty()) {
            User user = new User();
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setEmail(signUpRequest.getEmail());
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            User savedUser= userRepository.save(user);
            String jwt;
            String refreshToken;
            try{
                jwt=jwtService.generateToken(savedUser);
                refreshToken=jwtService.generateRefreshToken(new HashMap<>(),savedUser);
            }catch (Exception e){
                throw new RuntimeException("Token generation failed");
            }
            JwtAuthenticationRequest jwtAuthenticationRequest=new JwtAuthenticationRequest();
            jwtAuthenticationRequest.setToken(jwt);
            jwtAuthenticationRequest.setRefreshToken(refreshToken);
            return jwtAuthenticationRequest;
        }else{
            throw new IllegalArgumentException("user already exists!");
        }
    }

    @Override
    public JwtAuthenticationRequest signIn(SignInRequest signInRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
        User user=userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(()->new IllegalArgumentException("User not found"));
        String jwt;
        String refreshToken;

        try {
            jwt=jwtService.generateToken(user);
            refreshToken=jwtService.generateRefreshToken(new HashMap<>(),user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JwtAuthenticationRequest jwtAuthenticationRequest=new JwtAuthenticationRequest();
        jwtAuthenticationRequest.setToken(jwt);
        jwtAuthenticationRequest.setRefreshToken(refreshToken);
        return jwtAuthenticationRequest;
    }

    @Override
    public JwtAuthenticationRequest refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail=jwtService.extractUserName(refreshTokenRequest.getToken());
        User user=userRepository.findByEmail(userEmail).orElseThrow(()->new IllegalArgumentException("User not found!"));
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            Map<String, Object> extraClaims=new HashMap<>();
            var jwt=jwtService.generateRefreshToken(extraClaims,user);
            JwtAuthenticationRequest jwtAuthenticationRequest=new JwtAuthenticationRequest();
            jwtAuthenticationRequest.setToken(jwt);
            jwtAuthenticationRequest.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationRequest;
        }
        return null;
    }
}
