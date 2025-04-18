package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductResponseDTO toDto(Product product){
        ProductResponseDTO dto = new ProductResponseDTO();
        
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setImage(product.getImage());
        dto.setStock(product.getStock());
        dto.setIsDeleted(product.getIsDeleted());

        return dto;
    }

    public List<ProductResponseDTO> toDtos(List<Product> products){
        List<ProductResponseDTO> dtoList = new ArrayList<>();

        for (Product product : products) {
            ProductResponseDTO dto = this.toDto(product);
            dtoList.add(dto);
        }

        return dtoList;
    }

    public Product findById(Long id){
        Product result = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
        return result;
    }
}