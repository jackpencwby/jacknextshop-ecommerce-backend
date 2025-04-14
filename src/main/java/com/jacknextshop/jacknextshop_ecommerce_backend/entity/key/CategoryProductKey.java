package com.jacknextshop.jacknextshop_ecommerce_backend.entity.key;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CategoryProductKey implements Serializable{
    private static final long serialVersionUID = 1L;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "product_id")
    private Long productId;
}
