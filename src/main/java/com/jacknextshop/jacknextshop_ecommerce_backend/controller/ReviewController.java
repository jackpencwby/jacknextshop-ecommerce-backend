package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIPaginatedResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.review.ReviewRequestDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.review.ReviewResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Review;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.ReviewKey;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.ReviewRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.ProductService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.ReviewService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @PutMapping("/{productId}")
    public ResponseEntity<APIResponseDTO<?>> updateReview(
        OAuth2AuthenticationToken token,
        @PathVariable Long productId,
        @Valid @RequestBody ReviewRequestDTO reviewRequestDTO
    ){
        return this.createReview(token, productId, reviewRequestDTO);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<APIResponseDTO<?>> createReview(
        OAuth2AuthenticationToken token,
        @PathVariable Long productId,
        @Valid @RequestBody ReviewRequestDTO reviewRequestDTO
        ){
        User user = userService.getUserByToken(token);
        Product product = productService.findById(productId);
        if(product.getIsDeleted()){
            throw new ResourceNotFoundException("This Product is already deleted");
        }
        Review review = new Review();
        review.setReviewId(new ReviewKey(user.getUserId(), productId));
        review.setComment(reviewRequestDTO.getComment());
        review.setIsLike(reviewRequestDTO.isLike());
        review.setRating(reviewRequestDTO.getRating());
        review.setUser(user);
        review.setProduct(product);
        review.setIsDelete(false);
        Review savedReview = reviewRepository.save(review);

        // Create Response
        ReviewResponseDTO dto = reviewService.toDto(savedReview);
        APIResponseDTO<ReviewResponseDTO> responseDTO = new APIResponseDTO<>();
        responseDTO.setMessage("Success");
        responseDTO.setData(dto);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getReview(
        OAuth2AuthenticationToken token,
        @PathVariable Long productId,
        @RequestParam(defaultValue = "false") boolean onlyMe,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        Product product = productService.findById(productId);
        if(product.getIsDeleted()){
            throw new ResourceNotFoundException("This Product is already deleted");
        }
        if(onlyMe){
            User user = userService.getUserByToken(token);
            ReviewKey key = new ReviewKey();
            key.setProductId(productId);
            key.setUserId(user.getUserId());
            Review review = reviewService.findById(key);
            return ResponseEntity.ok(reviewService.toDto(review));
        }
        Page<Review> paginateReviews = reviewService.findAllByProductProductId(productId, page, size);
        List<ReviewResponseDTO> responseDtos = paginateReviews.getContent().stream().map(r -> reviewService.toDto(r)).toList();
        APIPaginatedResponseDTO<ReviewResponseDTO> response = new APIPaginatedResponseDTO<>();
        response.setData(responseDtos);
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(paginateReviews.getTotalPages());
        response.setTotalElements(paginateReviews.getTotalElements());

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<APIResponseDTO<?>> deleteReview(
        OAuth2AuthenticationToken token,
        @PathVariable Long productId,
        @RequestParam(required = false) Long userId
    ){
        User user = userService.getUserByToken(token);
        APIResponseDTO<ReviewResponseDTO> responseDTO = new APIResponseDTO<>();
        responseDTO.setMessage("Delete Success");

        if( ! user.getIsAdmin() || userId == null){
            // Delete their own review.
            ReviewKey key = new ReviewKey();
            key.setProductId(productId);
            key.setUserId(user.getUserId());
            Review review = reviewService.findById(key);
            if(review.getIsDelete()){
                throw new ResourceNotFoundException("Review is already deleted");
            }
            review.setIsDelete(true);
            reviewRepository.save(review);
            responseDTO.setData(reviewService.toDto(review));
            return ResponseEntity.ok().body(responseDTO);
        }
        // Delete their user's review.
        userService.checkAdmin(token);
        ReviewKey key = new ReviewKey();
        key.setProductId(productId);
        key.setUserId(userId);
        Review review = reviewService.findById(key);
        if(review.getIsDelete()){
            throw new ResourceNotFoundException("Review is already deleted");
        }
        review.setIsDelete(true);
        reviewRepository.save(review);
        responseDTO.setData(reviewService.toDto(review));
        return ResponseEntity.ok().body(responseDTO);
        }
}
