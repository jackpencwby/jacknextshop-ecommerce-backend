package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.review.ReviewResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Review;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.ReviewKey;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewResponseDTO toDto(Review review){
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setComment(review.getComment());
        dto.setLike(review.getIsLike());
        dto.setRating(review.getRating());
        dto.setProductId(review.getProduct().getProductId());
        dto.setUserId(review.getUser().getUserId());
        dto.setUserFname(review.getUser().getFname());
        return dto;
    }

    public Page<Review> findAllByProductProductId(Long productId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return reviewRepository.findAllByProductProductIdAndIsDeleteFalse(productId, pageable);
    }

    public Review findById(ReviewKey key){
        return reviewRepository.findById(key).orElseThrow(() -> new ResourceNotFoundException("Review Not found"));
    }

    public List<ReviewResponseDTO> toDtos(List<Review> reviews){
        List<ReviewResponseDTO> result = new ArrayList<>();
        for(Review r : reviews){
            ReviewResponseDTO dto = this.toDto(r);
            result.add(dto);
        }
        return result;
    }
}
