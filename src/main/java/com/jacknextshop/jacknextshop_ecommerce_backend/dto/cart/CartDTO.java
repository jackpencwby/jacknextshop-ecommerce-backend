package com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartDTO {
    private Long productId;
    private String name;
    private BigDecimal price;
    private String image;
    private int amount;
}
