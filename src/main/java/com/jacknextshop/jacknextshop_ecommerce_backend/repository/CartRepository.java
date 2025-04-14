package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CartKey;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartKey> {
}