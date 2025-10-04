package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIPaginatedResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.CreateProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductByProductId(@PathVariable UUID id) {
        Product product = productService.findByProductId(id);

        ProductDTO productDTO = productService.toDto(product);

        APIResponseDTO<ProductDTO> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(productDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<?> getPaginatedProduct(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> paginateProducts;

        if (categoryId == null) {
            paginateProducts = productService.getPaginatedAllProduct(page, size);
        } else {
            paginateProducts = productService.getPaginatedProductByCategoryId(categoryId, page, size);
        }

        List<ProductDTO> productDTOs = paginateProducts.getContent().stream()
                .map(p -> productService.toDto(p))
                .toList();

        APIPaginatedResponseDTO<ProductDTO> response = new APIPaginatedResponseDTO<>();

        response.setData(productDTOs);
        response.setTotalPages(paginateProducts.getTotalPages());
        response.setTotalElements(paginateProducts.getTotalElements());
        response.setPage(page);
        response.setSize(size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/best-seller")
    public ResponseEntity<?> getBestSellerProduct() {
        List<Product> products = productService.getBestSellerProduct();

        List<ProductDTO> productDTOs = productService.toDtos(products);

        APIResponseDTO<List<ProductDTO>> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(productDTOs);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<?> createProduct(
            @AuthenticationPrincipal OAuth2User principal,
            @Valid @ModelAttribute CreateProductDTO createProductDTO) {

        Product product = productService.createProduct(
                createProductDTO.getCategoryId(),
                createProductDTO.getName(),
                createProductDTO.getPrice(),
                createProductDTO.getDescription(),
                createProductDTO.getStock(),
                createProductDTO.getImage(),
                principal);

        ProductDTO productDTO = productService.toDto(product);

        APIResponseDTO<ProductDTO> response = new APIResponseDTO<>();
        response.setMessage("Create product successfully.");
        response.setData(productDTO);

        return ResponseEntity.ok(response);
    }
}
