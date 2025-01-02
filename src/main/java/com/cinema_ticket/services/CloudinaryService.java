package com.cinema_ticket.services;

import com.cinema_ticket.entities.Image;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    Image uploadFile(MultipartFile file, String folderName);
}
