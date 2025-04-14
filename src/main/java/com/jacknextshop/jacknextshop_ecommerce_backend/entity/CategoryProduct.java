package com.jacknextshop.jacknextshop_ecommerce_backend.entity;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CategoryProductKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "category_products")
public class CategoryProduct {
    @EmbeddedId
    private CategoryProductKey categoryProductKey;

    // Relations
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;
}
