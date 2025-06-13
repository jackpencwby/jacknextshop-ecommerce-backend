package com.jacknextshop.jacknextshop_ecommerce_backend.dto;

import lombok.Data;

@Data
public class APIResponseDTO<T> {
    private String message;
    private T data;
}
