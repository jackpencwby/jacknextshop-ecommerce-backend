package com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDTO;

import lombok.Data;

@Data
public class CartItemDTO {
    private ProductDTO productDTO;
    private Integer amount;
}
