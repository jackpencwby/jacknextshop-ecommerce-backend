package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.review.ReviewDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Review;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserForBiddenException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public List<Review> getReviewForProductByProductId(UUID productId) {
        return reviewRepository.findByProductProductIdAndIsDeleteFalse(productId);
    }

    public Review createReview(UUID productId, Integer rating, String comment, OAuth2User principal) {
        User user = userService.getCurrentUser(principal);

        Product product = productService.findByProductId(productId);

        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    public Review updateReview(
            UUID reviewId,
            Integer rating,
            String comment,
            OAuth2User principal) {

        User user = userService.getCurrentUser(principal);

        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found."));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new UserForBiddenException("Access denied to this review.");
        }

        if (rating != null) {
            review.setRating(rating);
        }
        if (comment != null && !comment.isBlank()) {
            review.setComment(comment);
        }

        return reviewRepository.save(review);
    }

    public ReviewDTO toDto(Review review) {
        ReviewDTO dto = new ReviewDTO();

        UUID reviewId = review.getReviewId();
        UUID userId = review.getUser().getUserId();

        Product product = productService.findByProductId(review.getProduct().getProductId());
        ProductDTO productDTO = productService.toDto(product);

        dto.setReviewId(reviewId);
        dto.setUserId(userId);
        dto.setProductDTO(productDTO);
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());

        return dto;
    }

    public List<ReviewDTO> toDtos(List<Review> review) {
        return review.stream().map(this::toDto).toList();
    }
}
