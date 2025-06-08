package com.jacknextshop.jacknextshop_ecommerce_backend.entity;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.OrderProductKey;

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
@Table(name = "order_products")
public class OrderProduct {
    @EmbeddedId
    private OrderProductKey orderProductId;

    // Fields
    @Column(name = "amount")
    private int amount;

    // Relationship
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
}
