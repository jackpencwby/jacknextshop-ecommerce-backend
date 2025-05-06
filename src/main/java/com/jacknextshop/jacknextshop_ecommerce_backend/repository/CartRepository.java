package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CartKey;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartKey> {
    List<Cart> findAllByIdUserId(Long userId);
    Optional<Cart> findById(CartKey cartKey);
}