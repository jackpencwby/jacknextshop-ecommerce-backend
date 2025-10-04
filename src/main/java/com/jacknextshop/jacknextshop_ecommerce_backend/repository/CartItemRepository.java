package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.CartItem;

import jakarta.transaction.Transactional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByUserUserId(UUID userId);
    Optional<CartItem> findByUserUserIdAndProductProductId(UUID userId, UUID productId);

    @Transactional
    void deleteByUserUserIdAndProductProductId(UUID userId, UUID productId);
}
