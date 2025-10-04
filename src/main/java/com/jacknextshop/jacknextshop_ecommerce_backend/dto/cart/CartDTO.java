package com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CartDTO {
    private UUID userId;
    private List<CartItemDTO> cartItemDTOs;
    private BigDecimal totalPrice;
}
