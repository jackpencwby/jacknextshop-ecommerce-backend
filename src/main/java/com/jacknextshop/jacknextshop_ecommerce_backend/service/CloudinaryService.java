package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.cloudinary.FileUploadException;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage());
        }
    }
}