package com.jacknextshop.jacknextshop_ecommerce_backend.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UpdateProductDTO {
    private UUID productId;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer stock;
    private Integer sold;
    private MultipartFile image;
}
