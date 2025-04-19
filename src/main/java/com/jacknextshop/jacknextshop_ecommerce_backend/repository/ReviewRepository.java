package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Review;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.ReviewKey;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewKey>{
    Page<Review> findAllByProductProductIdAndIsDeleteFalse(Long productId, Pageable pageable);
}
