package com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart;

import java.util.List;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductCartDto;

import lombok.Data;

@Data
public class ResponseCartDTO {
    private List<ProductCartDto> products;
    private String fname;
    private String lname;
    private long userId;
}
