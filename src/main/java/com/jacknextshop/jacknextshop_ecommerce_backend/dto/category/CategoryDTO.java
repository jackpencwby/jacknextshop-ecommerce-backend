package com.jacknextshop.jacknextshop_ecommerce_backend.dto.category;

import java.util.UUID;

import lombok.Data;

@Data
public class CategoryDTO {
    private UUID categoryId;
    private String name;
}
