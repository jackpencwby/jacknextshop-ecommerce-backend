package com.jacknextshop.jacknextshop_ecommerce_backend.dto.user;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserRequestBodyDTO{
    private String fname;
    private String lname;
    private String email;
    private LocalDate birthdate;
    private MultipartFile image;
}