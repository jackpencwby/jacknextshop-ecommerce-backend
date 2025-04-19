package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIPaginatedResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDto;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.ProductService;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping()
    public ResponseEntity<APIPaginatedResponseDTO<ProductDto>> getPaginatedAllProduct(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) 
    {
        Page<Product> paginateProducts = productService.getPaginatedAllProduct(page, size);

        List<ProductDto> productDtos = paginateProducts.getContent().stream().map(p -> productService.toDto(p)).toList();

        APIPaginatedResponseDTO<ProductDto> response = new APIPaginatedResponseDTO<>();

        response.setData(productDtos);
        response.setTotalPages(paginateProducts.getTotalPages());
        response.setTotalElements(paginateProducts.getTotalElements());
        response.setPage(page);
        response.setSize(size);

        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/{categoryId}")
    public ResponseEntity<APIPaginatedResponseDTO<ProductDto>> getPaginatedProductByCategoryId(
        @PathVariable Long categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) 
    {
        Page<Product> paginateProducts = productService.getPaginatedProductByCategoryId(categoryId, page, size);

        List<ProductDto> productDtos = paginateProducts.getContent().stream().map(p -> productService.toDto(p)).toList();

        APIPaginatedResponseDTO<ProductDto> response = new APIPaginatedResponseDTO<>();

        response.setData(productDtos);
        response.setTotalPages(paginateProducts.getTotalPages());
        response.setTotalElements(paginateProducts.getTotalElements());
        response.setPage(page);
        response.setSize(size);

        return ResponseEntity.ok(response);
    }
    
    // @PostMapping()
    // public ResponseEntity<APIResponseDTO<?>> createProduct(
    //     OAuth2AuthenticationToken token, 
    //     @Valid @ModelAttribute CreateProductDTO createProductDTO
    //     ) {
    //     userService.checkAdmin(token);
    //     Product product = new Product();
    //     product.setName(createProductDTO.getName());
    //     product.setPrice(createProductDTO.getPrice());
    //     product.setDescription(createProductDTO.getDescription());
    //     product.setStock(createProductDTO.getStock());
    //     try {
    //         String imageUrl = cloudinaryService.uploadImage(createProductDTO.getImage());
    //         product.setImage(imageUrl);
    //     } catch (Exception e){
    //         APIResponseDTO<?> response = new APIResponseDTO<>();
    //         response.setMessage("Server Error");
    //         response.setData(null);
    //         return ResponseEntity.badRequest().body(response);
    //     }

    //     Product savedProduct = productRepository.save(product);
    //     List<Category> categories = categoryService.findAllById(createProductDTO.getCategoriesId());
    //     for (Category c: categories){
    //         CategoryProductKey categoryProductKey = new CategoryProductKey(c.getCategoryId(), savedProduct.getProductId());
    //         CategoryProduct categoryProduct = new CategoryProduct();
    //         categoryProduct.setCategoryProductKey(categoryProductKey);
    //         categoryProduct.setCategory(c);
    //         categoryProduct.setProduct(savedProduct);
    //         categoryProductRepository.save(categoryProduct);
    //     }
    //     ProductResponseDTO dto = productService.toDto(savedProduct);
    //     APIResponseDTO<ProductResponseDTO> response = new APIResponseDTO<>();
    //     response.setMessage("Create Product Success");
    //     response.setData(dto);
    //     return ResponseEntity.ok().body(response);
    // }
}
