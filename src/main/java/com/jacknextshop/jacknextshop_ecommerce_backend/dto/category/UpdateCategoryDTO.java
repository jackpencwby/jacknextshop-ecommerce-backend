package com.jacknextshop.jacknextshop_ecommerce_backend.dto.category;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryDTO {
    private UUID categoryId;

    @NotBlank(message = "Category name is required.")
    private String name;
}
