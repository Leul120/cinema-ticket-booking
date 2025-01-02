package com.cinema_ticket.services.Impl;

import com.cinema_ticket.entities.Image;
import com.cinema_ticket.services.CloudinaryService;
import com.cloudinary.Cloudinary;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    @Override
    public Image uploadFile(MultipartFile file, String folderName) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);
            Image image=new Image();
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            image.setPublicId(publicId);
            image.setImageUrl(cloudinary.url().secure(true).generate(publicId));
            return image;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
