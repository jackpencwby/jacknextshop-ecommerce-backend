package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.OrderProduct;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.OrderProductKey;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey>{
    List<OrderProduct> findAllByOrderOrderId(Long orderId);
}
