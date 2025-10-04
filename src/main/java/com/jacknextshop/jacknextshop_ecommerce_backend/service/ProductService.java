package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserForBiddenException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserService userService;

    public Product findByProductId(UUID productId) {
        return productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
    }

    public Page<Product> getPaginatedAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAllByIsDeletedFalse(pageable);
    }

    public Page<Product> getPaginatedProductByCategoryId(UUID categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategoryCategoryIdAndIsDeletedFalse(categoryId, pageable);
    }

    public List<Product> getBestSellerProduct() {
        return productRepository.findTop5ByOrderBySoldDesc();
    }

    public Product createProduct(
            UUID categoryId,
            String name,
            BigDecimal price,
            String description,
            int stock,
            MultipartFile image,
            OAuth2User principal) {

        if (!userService.isAdmin(principal)) {
            throw new UserForBiddenException("Only admin can create product.");
        }

        Category category = categoryService.findByCategoryId(categoryId);

        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setStock(stock);
        product.setImage(cloudinaryService.uploadFile(image));

        return productRepository.save(product);
    }

    public Product updateProduct(
            UUID productId,
            String name,
            BigDecimal price,
            String description,
            Integer stock,
            Integer sold,
            MultipartFile image,
            OAuth2User principal) {

        if (!userService.isAdmin(principal)) {
            throw new UserForBiddenException("Only admin can update product.");
        }

        Product product = findByProductId(productId);

        if (name != null && !name.isBlank()) {
            product.setName(name);
        }
        if (price != null) {
            product.setPrice(price);
        }
        if (description != null && !description.isBlank()) {
            product.setDescription(description);
        }
        if (stock != null) {
            product.setStock(stock);
        }
        if (sold != null) {
            product.setSold(sold);
        }
        if (image != null) {
            product.setImage(cloudinaryService.uploadFile(image));
        }

        return productRepository.save(product);
    }

    public Product deleteProduct(UUID productId, OAuth2User principal) {
        if (!userService.isAdmin(principal)) {
            throw new UserForBiddenException("Only admin can delete product.");
        }

        Product product = findByProductId(productId);

        product.setIsDeleted(true);

        return productRepository.save(product);
    }

    public ProductDTO toDto(Product product) {
        ProductDTO dto = new ProductDTO();

        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setStock(product.getStock());
        dto.setSold(product.getSold());
        dto.setImage(product.getImage());

        return dto;
    }

    public List<ProductDTO> toDtos(List<Product> products) {
        return products.stream().map(p -> toDto(p)).toList();
    }
}