package com.jacknextshop.jacknextshop_ecommerce_backend.dto.review;

import java.util.List;

import lombok.Data;

@Data
public class ReviewPaginationDTO<T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int page;
    private Double rating;
    private int size;
}
