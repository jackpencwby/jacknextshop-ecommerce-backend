package com.jacknextshop.jacknextshop_ecommerce_backend.dto.orderproduct;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDto;

import lombok.Data;

@Data
public class OrderProductDTO {
    private Long orderId; 
    private ProductDto productDto;
    private int amount;
}
