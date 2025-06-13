package com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CartListDTO {
    private Long userId;
    private List<CartDTO> carts;
    private BigDecimal totalPrice;
}
