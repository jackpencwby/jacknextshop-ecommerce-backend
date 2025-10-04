package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.category.CategoryDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserForBiddenException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    public List<Category> findAll() {
        return categoryRepository.findAllByIsDeletedFalse();
    }

    public Category findByCategoryId(UUID categoryId) {
        return categoryRepository.findByCategoryIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
    }

    public Category createCategory(String name, OAuth2User principal) {
        if (!userService.isAdmin(principal)) {
            throw new UserForBiddenException("Only admin can create category.");
        }

        Category category = new Category();

        category.setName(name);

        return categoryRepository.save(category);
    }

    public CategoryDTO toDto(Category category) {
        CategoryDTO dto = new CategoryDTO();

        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());

        return dto;
    }

    public List<CategoryDTO> toDtos(List<Category> categories) {
        return categories.stream().map(this::toDto).toList();
    }
}
