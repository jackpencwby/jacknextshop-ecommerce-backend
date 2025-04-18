package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.CreateProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Category;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.CategoryProduct;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CategoryProductKey;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CategoryProductRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.ProductRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CategoryService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CloudinaryService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.ProductService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<APIResponseDTO<?>> getProduct(){
        List<Product> products = productRepository.findAll();
        List<ProductResponseDTO> productDTOs = productService.toDtos(products);
        // Filter Deleted products
        List<ProductResponseDTO> filtered = productDTOs.stream()
            .filter(dto -> ! dto.getIsDeleted())
            .collect(Collectors.toList());

        APIResponseDTO<List<ProductResponseDTO>> response = new APIResponseDTO<>();
        response.setMessage("Success");
        response.setData(filtered);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping()
    public ResponseEntity<APIResponseDTO<?>> createProduct(
        OAuth2AuthenticationToken token, 
        @Valid @ModelAttribute CreateProductDTO createProductDTO
        ) {
        userService.checkAdmin(token);
        Product product = new Product();
        product.setName(createProductDTO.getName());
        product.setPrice(createProductDTO.getPrice());
        product.setDescription(createProductDTO.getDescription());
        product.setStock(createProductDTO.getStock());
        try {
            String imageUrl = cloudinaryService.uploadImage(createProductDTO.getImage());
            product.setImage(imageUrl);
        } catch (Exception e){
            APIResponseDTO<?> response = new APIResponseDTO<>();
            response.setMessage("Server Error");
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }

        Product savedProduct = productRepository.save(product);
        List<Category> categories = categoryService.findAllById(createProductDTO.getCategoriesId());
        for (Category c: categories){
            CategoryProductKey categoryProductKey = new CategoryProductKey(c.getCategoryId(), savedProduct.getProductId());
            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.setCategoryProductKey(categoryProductKey);
            categoryProduct.setCategory(c);
            categoryProduct.setProduct(savedProduct);
            categoryProductRepository.save(categoryProduct);
        }
        ProductResponseDTO dto = productService.toDto(savedProduct);
        APIResponseDTO<ProductResponseDTO> response = new APIResponseDTO<>();
        response.setMessage("Create Product Success");
        response.setData(dto);
        return ResponseEntity.ok().body(response);
    }
}
