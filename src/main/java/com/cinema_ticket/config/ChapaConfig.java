package com.cinema_ticket.config;

import com.yaphet.chapa.Chapa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChapaConfig {
    @Bean
    public Chapa Chapa(){
       return new Chapa("CHASECK_TEST-EqQOEHfuqjYtcueTtuMht1oMicnQNczz");
    }
}
