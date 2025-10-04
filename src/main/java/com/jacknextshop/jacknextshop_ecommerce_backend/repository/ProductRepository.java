package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByProductIdAndIsDeletedFalse(UUID productId);
    Page<Product> findAllByIsDeletedFalse(Pageable pageable);
    Page<Product> findByCategoryCategoryIdAndIsDeletedFalse(UUID cateoryId, Pageable pageable);
    List<Product> findTop5ByOrderBySoldDesc();
}
