package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jacknextshop.jacknextshop_ecommerce_backend.service.CloudinaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class FileUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = cloudinaryService.uploadFile(file);

        return ResponseEntity.ok(imageUrl);
    }
}
