package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    Page<Product> findAllByIsDeletedFalse(Pageable pageable);
    Page<Product> findByCategoryCategoryIdAndIsDeletedFalse(Long cateoryId, Pageable pageable);
}
