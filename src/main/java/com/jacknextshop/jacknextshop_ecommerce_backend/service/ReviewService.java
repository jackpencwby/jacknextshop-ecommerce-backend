package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.review.ReviewResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Review;

@Service
public class ReviewService {
    public ReviewResponseDTO toDto(Review review){
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setComment(review.getComment());
        dto.setLike(review.getIsLike());
        dto.setRating(review.getRating());
        dto.setProductId(review.getProduct().getProductId());
        dto.setUserId(review.getUser().getUserId());
        return dto;
    }
}
