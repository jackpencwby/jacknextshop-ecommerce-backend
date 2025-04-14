package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.category.CreateCategoryDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping()
    public ResponseEntity<?> createCategory(@ModelAttribute CreateCategoryDTO createCategoryDTO) {
        try {
            Category category = new Category();
            category.setName(createCategoryDTO.getName());

            categoryRepository.save(category);

            return ResponseEntity.ok(category);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
