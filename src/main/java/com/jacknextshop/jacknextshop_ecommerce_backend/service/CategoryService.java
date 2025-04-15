package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.category.CategoryResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(Long categoryId){
        Category result = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
        return result;
    }

    public List<Category> findAllById(List<Long> Ids){
        List<Category> result = categoryRepository.findAllById(Ids);
        return result;
    }

    public CategoryResponseDTO toDto(Category category){
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setName(category.getName());
        return dto;
    }
}
