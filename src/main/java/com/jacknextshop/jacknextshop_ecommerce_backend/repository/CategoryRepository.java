package com.jacknextshop.jacknextshop_ecommerce_backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
}