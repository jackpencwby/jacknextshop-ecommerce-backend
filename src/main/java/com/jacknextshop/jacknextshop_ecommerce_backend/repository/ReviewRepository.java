package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
