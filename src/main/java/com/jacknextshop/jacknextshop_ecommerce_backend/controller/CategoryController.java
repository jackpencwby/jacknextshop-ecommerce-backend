package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.category.CategoryResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.category.CreateCategoryDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CategoryRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CategoryService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getCategory(){
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDTO> dtos = categoryService.toDtos(categories);
        
        List<CategoryResponseDTO> filtered = dtos.stream()
            .filter(dto -> ! dto.isDelete())
            .collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @PostMapping()
    public ResponseEntity<?> createCategory( OAuth2AuthenticationToken token, 
    @Valid @RequestBody CreateCategoryDTO createCategoryDTO
    ) {
        userService.checkAdmin(token);
        try {
            Category category = new Category();
            category.setName(createCategoryDTO.getName());

            categoryRepository.save(category);

            return ResponseEntity.ok(categoryService.toDto(category));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory( OAuth2AuthenticationToken token, 
        @Valid @RequestBody CreateCategoryDTO createCategoryDTO, 
        @PathVariable Long id 
    ){
        userService.checkAdmin(token);
        Category category = categoryService.findById(id);
        if(category.getIsDeleted()){
            throw new ResourceNotFoundException("This category is already deleted.");
        }
        category.setName(createCategoryDTO.getName());
        categoryRepository.save(category);
        var dto = categoryService.toDto(category);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(OAuth2AuthenticationToken token, @PathVariable Long id){
        userService.checkAdmin(token);
        Category category = categoryService.findById(id);
        if(category.getIsDeleted()){
            throw new ResourceNotFoundException("This category is already deleted.");
        }
        category.setIsDeleted(true);
        categoryRepository.save(category);
        var dto = categoryService.toDto(category);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailCategory(@PathVariable Long id){

        Category category = categoryService.findById(id);
        if(category.getIsDeleted()){
            throw new ResourceNotFoundException("This category is already deleted.");
        }
        var dto = categoryService.toDto(category);
        return ResponseEntity.ok().body(dto);
    }
}
