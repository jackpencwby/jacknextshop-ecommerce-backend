package com.jacknextshop.jacknextshop_ecommerce_backend.dto.order;

import java.util.UUID;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDTO;

import lombok.Data;

@Data
public class OrderItemDTO {
    private UUID orderId;
    private ProductDTO productDTO;
    private int amount;
}