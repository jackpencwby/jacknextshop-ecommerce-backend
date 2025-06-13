package com.jacknextshop.jacknextshop_ecommerce_backend.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.address.AddressDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.orderproduct.OrderProductDTO;

import lombok.Data;

@Data
public class OrderDTO {
    private Long orderId;
    private AddressDTO addressDTO;
    private List<OrderProductDTO> orderProductDTOs;
    private BigDecimal totalPrice;
    private String status;
    private LocalDate orderDate;
    private LocalDate shippingDate;
}
