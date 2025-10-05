package com.jacknextshop.jacknextshop_ecommerce_backend.dto.review;

import java.util.UUID;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDTO;

import lombok.Data;

@Data
public class ReviewDTO {
    private UUID reviewId;
    private UUID userId;
    private ProductDTO productDTO;
    private Integer rating;
    private String comment;
}
