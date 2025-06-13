package com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart;

import java.util.List;

import lombok.Data;

@Data
public class CartListDTO {
    private Long userId;
    private List<CartDTO> carts;
}
