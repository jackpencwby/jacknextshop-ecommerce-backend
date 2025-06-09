package com.jacknextshop.jacknextshop_ecommerce_backend.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryDTO {

    @NotBlank(message = "กรุณาใส่ชื่อหมวดหมู่")
    private String name;
}
