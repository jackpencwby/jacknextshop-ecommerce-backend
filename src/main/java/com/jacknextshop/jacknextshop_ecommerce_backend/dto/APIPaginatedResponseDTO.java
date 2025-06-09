package com.jacknextshop.jacknextshop_ecommerce_backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class APIPaginatedResponseDTO<T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;
}
