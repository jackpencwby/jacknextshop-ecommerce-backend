package com.jacknextshop.jacknextshop_ecommerce_backend.dto.user;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String fname;
    private String lname;
    private LocalDate birthDate;
    private String email;
    private String image;
}
