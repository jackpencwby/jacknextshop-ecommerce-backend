package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIPaginatedResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.CreateProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDto;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.ProductService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

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

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<APIPaginatedResponseDTO<ProductDto>> getPaginatedProduct(
        @RequestParam(required = false) Long categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) 
    {
        Page<Product> paginateProducts;

        if(categoryId == null){
            paginateProducts = productService.getPaginatedAllProduct(page, size);
        }
        else{
            paginateProducts = productService.getPaginatedProductByCategoryId(categoryId, page, size);
        }

        List<ProductDto> productDtos = paginateProducts.getContent().stream()
        .map(p -> productService.toDto(p))
        .toList();
        
        APIPaginatedResponseDTO<ProductDto> response = new APIPaginatedResponseDTO<>();

        response.setData(productDtos);
        response.setTotalPages(paginateProducts.getTotalPages());
        response.setTotalElements(paginateProducts.getTotalElements());
        response.setPage(page);
        response.setSize(size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<APIResponseDTO<ProductDto>> getProductById(@PathVariable("product_id") Long productId){
        Product product = productService.findById(productId);

        ProductDto productDto = productService.toDto(product);

        APIResponseDTO<ProductDto> response = new APIResponseDTO<>();
        response.setMessage("Success");
        response.setData(productDto);

        return ResponseEntity.ok(response);
    }
    
    @PostMapping()
    public ResponseEntity<APIResponseDTO<ProductDto>> createProduct(
        OAuth2AuthenticationToken token,
        @Valid @ModelAttribute CreateProductDTO createProductDTO
    )
    {
        userService.checkAdmin(token);

        Product product = productService.createProduct(
            createProductDTO.getCategoryId(), 
            createProductDTO.getName(), 
            createProductDTO.getPrice(), 
            createProductDTO.getDescription(), 
            createProductDTO.getStock(),
            createProductDTO.getImage()
        );

        ProductDto productDto = productService.toDto(product);

        APIResponseDTO<ProductDto> response = new APIResponseDTO<>();
        response.setMessage("Create product successfully.");
        response.setData(productDto);

        return ResponseEntity.ok(response);
    }
}
