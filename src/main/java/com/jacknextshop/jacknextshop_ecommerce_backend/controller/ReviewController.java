package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.review.CreateReviewDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.review.ReviewDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.review.UpdateReviewDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Review;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.ReviewService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductReview(@PathVariable UUID id) {
        List<Review> reviews = reviewService.getReviewForProductByProductId(id);

        List<ReviewDTO> reviewDTOs = reviewService.toDtos(reviews);

        APIResponseDTO<List<ReviewDTO>> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(reviewDTOs);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<?> createReview(
            @Valid @ModelAttribute CreateReviewDTO createReviewDTO,
            @AuthenticationPrincipal OAuth2User principal) {

        Review review = reviewService.createReview(
                createReviewDTO.getProductId(),
                createReviewDTO.getRating(),
                createReviewDTO.getComment(),
                principal);

        ReviewDTO reviewDTO = reviewService.toDto(review);

        APIResponseDTO<ReviewDTO> response = new APIResponseDTO<>();
        response.setMessage("Create review successfully.");
        response.setData(reviewDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<?> updateReview(
            @AuthenticationPrincipal OAuth2User principal,
            @Valid @ModelAttribute UpdateReviewDTO updateReviewDTO) {

        Review review = reviewService.updateReview(
                updateReviewDTO.getReviewId(),
                updateReviewDTO.getRating(),
                updateReviewDTO.getComment(),
                principal);

        ReviewDTO reviewDTO = reviewService.toDto(review);

        APIResponseDTO<ReviewDTO> response = new APIResponseDTO<>();
        response.setMessage("Update review successfully.");
        response.setData(reviewDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable UUID id) {

        Review review = reviewService.deleteReview(id, principal);

        ReviewDTO reviewDTO = reviewService.toDto(review);

        APIResponseDTO<ReviewDTO> response = new APIResponseDTO<>();
        response.setMessage("Delete review successfully.");
        response.setData(reviewDTO);

        return ResponseEntity.ok(response);
    }
}
