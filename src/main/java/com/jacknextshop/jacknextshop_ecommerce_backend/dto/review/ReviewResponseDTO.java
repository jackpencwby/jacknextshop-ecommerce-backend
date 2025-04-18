package com.jacknextshop.jacknextshop_ecommerce_backend.dto.review;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private String comment;
    private int rating;
    private boolean isLike;
    private Long userId;
    private String userFname;
    private Long productId;
}
