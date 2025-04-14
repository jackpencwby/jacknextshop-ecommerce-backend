package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.CategoryProduct;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CategoryProductKey;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, CategoryProductKey>{
}
