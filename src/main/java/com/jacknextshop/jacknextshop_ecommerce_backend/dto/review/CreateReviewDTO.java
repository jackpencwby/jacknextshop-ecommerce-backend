package com.jacknextshop.jacknextshop_ecommerce_backend.dto.review;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateReviewDTO {
    private UUID productId;
    private Integer rating;
    private String comment;
}
