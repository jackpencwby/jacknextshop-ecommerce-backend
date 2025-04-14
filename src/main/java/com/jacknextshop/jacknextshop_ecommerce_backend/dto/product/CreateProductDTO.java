package com.jacknextshop.jacknextshop_ecommerce_backend.dto.product;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductDTO {

    @NotBlank(message = "กรุณาใส่ชื่อหมวดหมู่")
    private String name;

    @NotNull(message = "กรุณาใส่ราคาสินค้า")
    @DecimalMin(value = "0.01", message = "ราคาสินค้าต้องมากกว่า 0")
    private BigDecimal price;

    @NotBlank(message = "กรุณาใส่ชื่อหมวดหมู่")
    private String description;

    @NotNull(message = "กรุณาใส่ราคาสินค้า")
    @Min(value = 0, message = "ราคาสินค้าต้องมากกว่าหรือเท่ากับ 0")
    private Integer stock;
    
    private MultipartFile image;
}
