package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDto;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired 
    private CategoryService categoryService;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
    }

    public ProductDto toDto(Product product){
        ProductDto dto = new ProductDto();

        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setStock(product.getStock());
        dto.setImage(product.getImage());

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

    public Product createProduct(
        Long categoryId, 
        String name, 
        BigDecimal price, 
        String description,
        int stock,
        MultipartFile image
    )
    {
        Category category = categoryService.findById(categoryId);

        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setStock(stock);
        product.setImage(cloudinaryService.uploadImage(image));

        return productRepository.save(product);
    }
}