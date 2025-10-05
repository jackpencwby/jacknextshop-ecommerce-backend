package com.jacknextshop.jacknextshop_ecommerce_backend.dto.order;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateOrderDTO {
    private UUID addressId;
}
