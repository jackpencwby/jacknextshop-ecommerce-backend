package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Review;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.ReviewKey;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewKey>{
    List<Review> findAllByProductProductId(Long productId);

}
