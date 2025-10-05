package com.jacknextshop.jacknextshop_ecommerce_backend.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.address.AddressDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Order.OrderStatus;

import lombok.Data;

@Data
public class OrderDTO {
    private UUID orderId;
    private AddressDTO addressDTO;
    private List<OrderItemDTO> orderItemDTOs;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDate orderDate;
    private LocalDate shippingDate;
}
