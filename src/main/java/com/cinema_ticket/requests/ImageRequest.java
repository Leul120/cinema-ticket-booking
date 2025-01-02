package com.cinema_ticket.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageRequest {
    private String name;
    private MultipartFile file;
}
