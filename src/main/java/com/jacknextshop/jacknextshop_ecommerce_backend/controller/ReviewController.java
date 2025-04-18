package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Review savedReview = reviewRepository.save(review);

        // Create Response
        ReviewResponseDTO dto = reviewService.toDto(savedReview);
        APIResponseDTO<ReviewResponseDTO> responseDTO = new APIResponseDTO<>();
        responseDTO.setMessage("Success");
        responseDTO.setData(dto);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<APIResponseDTO<?>> getReview(
        @PathVariable Long productId
    ){
        Product product = productService.findById(productId);
        if(product.getIsDeleted()){
            throw new ResourceNotFoundException("This Product is already deleted");
        }
        List<Review> reviews = reviewService.findAllByProductProductId(productId);
        List<ReviewResponseDTO> result = reviewService.toDtos(reviews);
        APIResponseDTO<List<ReviewResponseDTO>> responseDTO = new APIResponseDTO<>();
        responseDTO.setMessage("Success");
        responseDTO.setData(result);
        return ResponseEntity.ok().body(responseDTO);
    }
}
