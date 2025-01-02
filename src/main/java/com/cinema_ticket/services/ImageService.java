package com.cinema_ticket.services;

import com.cinema_ticket.entities.Image;
import com.cinema_ticket.requests.ImageRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ImageService {
    Image uploadImage(ImageRequest imageModel);
}
