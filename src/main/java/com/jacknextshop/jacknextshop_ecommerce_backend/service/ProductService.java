package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDto;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product findById(Long id){
        Product result = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
        return result;
    }

    public ProductDto toDto(Product product){
        ProductDto dto = new ProductDto();

        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setStock(product.getStock());
        dto.setImage(product.getImage());
        dto.setIsDeleted(product.getIsDeleted());

        return dto;
    }

    public List<ProductDto> toDtos(List<Product> products){
        List<ProductDto> dtos = products.stream().map(p -> toDto(p)).toList();

        return dtos;
    }

    public Page<Product> getPaginatedAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllByIsDeletedFalse(pageable);
    } 

    public Page<Product> getPaginatedProductByCategoryId(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategoryCategoryIdAndIsDeletedFalse(categoryId, pageable);
    }
}