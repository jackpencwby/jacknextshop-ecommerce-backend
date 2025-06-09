package com.jacknextshop.jacknextshop_ecommerce_backend.dto.category;

import lombok.Data;

@Data
public class CategoryResponseDTO {
    private Long categoryId;
    private String name;
    private boolean isDelete;
}
