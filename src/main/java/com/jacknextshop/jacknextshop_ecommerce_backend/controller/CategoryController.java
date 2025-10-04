package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.category.CategoryDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.category.CreateCategoryDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.category.UpdateCategoryDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CategoryService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<?> getAllCategory() {
        List<Category> categories = categoryService.findAll();

        List<CategoryDTO> categoryDTOs = categoryService.toDtos(categories);

        APIResponseDTO<List<CategoryDTO>> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(categoryDTOs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryByCategoryId(@PathVariable UUID id) {
        Category category = categoryService.findByCategoryId(id);

        CategoryDTO categoryDTO = categoryService.toDto(category);

        APIResponseDTO<CategoryDTO> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(categoryDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<?> createCategory(
            @Valid @ModelAttribute CreateCategoryDTO createCategoryDTO,
            @AuthenticationPrincipal OAuth2User principal) {

        Category category = categoryService.createCategory(
                createCategoryDTO.getName(),
                principal);

        CategoryDTO categoryDTO = categoryService.toDto(category);

        APIResponseDTO<CategoryDTO> response = new APIResponseDTO<>();
        response.setMessage("Create category successfully.");
        response.setData(categoryDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<?> updateCategory(
            @AuthenticationPrincipal OAuth2User principal,
            @Valid @ModelAttribute UpdateCategoryDTO updateCategoryDTO) {

        Category category = categoryService.updateCategory(
                updateCategoryDTO.getCategoryId(),
                updateCategoryDTO.getName(),
                principal);

        CategoryDTO categoryDTO = categoryService.toDto(category);

        APIResponseDTO<CategoryDTO> response = new APIResponseDTO<>();
        response.setData(categoryDTO);
        response.setMessage("Update category successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable UUID id) {

        Category category = categoryService.deleteCategory(id, principal);

        CategoryDTO categoryDTO = categoryService.toDto(category);

        APIResponseDTO<CategoryDTO> response = new APIResponseDTO<>();
        response.setData(categoryDTO);
        response.setMessage("Delete category successfully");

        return ResponseEntity.ok(response);
    }
}
