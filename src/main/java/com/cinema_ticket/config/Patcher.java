package com.cinema_ticket.config;

import com.cinema_ticket.entities.Review;
import com.cinema_ticket.entities.Seat;
import com.cinema_ticket.entities.SeatStatus;
import com.cinema_ticket.requests.SeatRequest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class Patcher {
    public static void seatPatcher(Seat existingSeat, Seat incompleteSeat) throws IllegalAccessException {
        Class<?> seatClass=Seat.class;
        Field[] seatFields=seatClass.getDeclaredFields();
        for (Field field:seatFields){
            field.setAccessible(true);
            Object value=field.get(incompleteSeat);
            if (value!=null){
                field.set(existingSeat,value);
            }
            field.setAccessible(false);
        }
    }
    public static void seatStatusPatcher(SeatStatus existingSeatStatus, SeatStatus incompleteSeatStatus) throws IllegalAccessException {
        Class<?> seatStatusClass=SeatStatus.class;
        Field[] seatStatusFields=seatStatusClass.getDeclaredFields();
        for (Field field:seatStatusFields){
            field.setAccessible(true);
            Object value=field.get(incompleteSeatStatus);
            if (value!=null){
                field.set(existingSeatStatus,value);
            }
            field.setAccessible(false);
        }
    }
    public static void reviewPatcher(Review existingReview, Review incompleteReview) throws IllegalAccessException {
        Class<?> reviewClass=Review.class;
        Field[] reviewFields=reviewClass.getDeclaredFields();
        for (Field field:reviewFields){
            field.setAccessible(true);
            Object value=field.get(incompleteReview);
            if (value!=null){
                field.set(existingReview,value);
            }
            field.setAccessible(false);
        }
    }
}

