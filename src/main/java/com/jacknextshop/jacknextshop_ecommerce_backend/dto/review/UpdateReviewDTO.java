package com.jacknextshop.jacknextshop_ecommerce_backend.dto.review;

import java.util.UUID;

import lombok.Data;

@Data
public class UpdateReviewDTO {
    private UUID reviewId;
    private Integer rating;
    private String comment;
}
