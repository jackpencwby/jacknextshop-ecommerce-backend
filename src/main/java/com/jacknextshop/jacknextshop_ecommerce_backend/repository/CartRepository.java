package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CartKey;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartKey> {
    Optional<Cart> findByUserUserIdAndProductProductId(Long userId, Long productId);
    
    Optional<List<Cart>> findAllByUserUserId(Long userId);

    @Transactional
    void deleteByUserUserIdAndProductProductId(Long userId, Long productId);
}