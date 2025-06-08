package com.jacknextshop.jacknextshop_ecommerce_backend.entity;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.ReviewKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "reviews")
public class Review {
    @EmbeddedId
    private ReviewKey reviewId;

    // Fields
    @Column(name = "comment")
    private String comment;

    @Column(name = "rating")
    private int rating;

    @Column(name = "is_like")
    private Boolean isLike;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    // Relationship
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
}
