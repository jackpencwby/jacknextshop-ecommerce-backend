package com.jacknextshop.jacknextshop_ecommerce_backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    // Relationship
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CategoryProduct> categoryProducts;


    // Fields
    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
