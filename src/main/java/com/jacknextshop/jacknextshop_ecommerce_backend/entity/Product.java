package com.jacknextshop.jacknextshop_ecommerce_backend.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "sold")
    private Integer sold = 0;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
