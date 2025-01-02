package com.cinema_ticket.services.Impl;

import com.cinema_ticket.entities.Image;
import com.cinema_ticket.repositories.ImageRepository;
import com.cinema_ticket.requests.ImageRequest;
import com.cinema_ticket.services.CloudinaryService;
import com.cinema_ticket.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public Image uploadImage(ImageRequest imageModel) {
        try {
            if (imageModel.getName().isEmpty()) {
                throw  new RuntimeException("Empty file");
            }
            if (imageModel.getFile().isEmpty()) {
                throw  new RuntimeException("Empty file");
            }
            Image image =cloudinaryService.uploadFile(imageModel.getFile(), "folder_1");


            if(image.getImageUrl() == null) {
                throw  new RuntimeException("Empty file");
            }

            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
