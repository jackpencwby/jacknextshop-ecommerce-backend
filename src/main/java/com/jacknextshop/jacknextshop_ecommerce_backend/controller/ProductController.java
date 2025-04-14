package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.CreateProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.CategoryProduct;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CategoryProductKey;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CategoryProductRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CategoryRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.ProductRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CloudinaryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping()
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute CreateProductDTO createProductDTO) {
        try {
            Product product = new Product();
            product.setName(createProductDTO.getName());
            product.setPrice(createProductDTO.getPrice());
            product.setDescription(createProductDTO.getDescription());
            product.setStock(createProductDTO.getStock());

            String imageUrl = cloudinaryService.uploadImage(createProductDTO.getImage());
            product.setImage(imageUrl);

            Product savedProduct = productRepository.save(product);

            for (Long categoryId : createProductDTO.getCategoriesId()) {
                Category category = categoryRepository.findById(categoryId).orElse(null);

                CategoryProductKey categoryProductKey = new CategoryProductKey(categoryId, savedProduct.getProductId());

                CategoryProduct categoryProduct = new CategoryProduct();
                categoryProduct.setCategoryProductKey(categoryProductKey);
                categoryProduct.setCategory(category);
                categoryProduct.setProduct(savedProduct);

                categoryProductRepository.save(categoryProduct);
            }

            return ResponseEntity.ok(product);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
