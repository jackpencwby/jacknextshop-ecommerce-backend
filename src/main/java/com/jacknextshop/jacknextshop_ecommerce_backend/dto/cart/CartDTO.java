package com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDto;

import lombok.Data;

@Data
public class CartDTO {
    private Long userId;
    private ProductDto productDto;
}
