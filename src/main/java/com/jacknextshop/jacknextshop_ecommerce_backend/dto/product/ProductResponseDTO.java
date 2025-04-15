package com.jacknextshop.jacknextshop_ecommerce_backend.dto.product;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private BigDecimal price;
    private String description;
    private List<Long> categoryIds;
    private String image;
    private int stock;
    private Boolean isDeleted;
}
