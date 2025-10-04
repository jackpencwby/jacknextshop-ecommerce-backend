package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
}
