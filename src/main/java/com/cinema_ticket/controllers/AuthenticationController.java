package com.cinema_ticket.controllers;


import com.cinema_ticket.entities.User;
import com.cinema_ticket.repositories.UserRepository;
import com.cinema_ticket.requests.RefreshTokenRequest;
import com.cinema_ticket.requests.SignInRequest;
import com.cinema_ticket.requests.SignUpRequest;
import com.cinema_ticket.requests.TokenRequest;
import com.cinema_ticket.responses.ApiResponse;
import com.cinema_ticket.services.AuthenticationService;
import com.cinema_ticket.services.BookingService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.yaphet.chapa.Chapa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final Chapa chapa;
    private final BookingService bookingService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,UserRepository userRepository,Chapa chapa,BookingService bookingService){
        this.authenticationService=authenticationService;
        this.userRepository=userRepository;
        this.chapa=chapa;
        this.bookingService=bookingService;
    }
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignUpRequest signUpRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",authenticationService.signup(signUpRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequest signInRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("success",authenticationService.signIn(signInRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/google")
    public ResponseEntity<ApiResponse> googleSignUp(@RequestBody TokenRequest request){
        try {
            System.out.println(request);
//            System.out.println(googleClientId);
            GoogleIdTokenVerifier verifier=new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            ).setAudience(Collections.singletonList(googleClientId))
                    .build();
            GoogleIdToken idToken=verifier.verify(request.getCredential());
            if(idToken!=null){
                GoogleIdToken.Payload payload=idToken.getPayload();
                String email= payload.getEmail();
                String sub= payload.getSubject();
                String firstName=(String) payload.get("given_name");
                String lastName=(String) payload.get("family_name");
                Optional<User> user=userRepository.findByEmail(email);
                System.out.println(user);
                if(user.isEmpty()){
                    SignUpRequest signUpRequest=new SignUpRequest();
                    signUpRequest.setEmail(email);
                    signUpRequest.setFirstName(firstName);
                    signUpRequest.setLastName(lastName);
                    signUpRequest.setPassword(sub);
                    return ResponseEntity.ok(new ApiResponse("success",authenticationService.signup(signUpRequest)));
                }else{
                    SignInRequest signInRequest=new SignInRequest();
                    signInRequest.setEmail(email);
                    signInRequest.setPassword(sub);
                    return ResponseEntity.ok(new ApiResponse("success",authenticationService.signIn(signInRequest)));
                }
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse("Invalid ID token.",HttpStatus.UNAUTHORIZED));
            }
        } catch (GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error verifying token: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error verifying token: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        try {
            return ResponseEntity.ok(new ApiResponse("success",authenticationService.refreshToken(request)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/verify-payment/{id}")
    public ResponseEntity<ApiResponse> initializePayment(@PathVariable Long id,@RequestParam("trx_ref") String transaction){
        try {
            chapa.verify(transaction);
            System.out.println("verify");
            return ResponseEntity.ok(new ApiResponse("success",bookingService.confirmBooking(id)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
