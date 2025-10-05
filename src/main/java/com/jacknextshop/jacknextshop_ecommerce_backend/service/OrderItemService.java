package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.order.OrderItemDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Order;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.OrderItem;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.OrderItemRepository;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductService productService;

    public OrderItem createOrderProduct(Order order, Product product, Integer amount) {
        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setAmount(amount);

        return orderItemRepository.save(orderItem);
    }

    public OrderItemDTO toDto(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();

        UUID orderId = orderItem.getOrder().getOrderId();
        ProductDTO productDTO = productService.toDto(orderItem.getProduct());
        Integer amount = orderItem.getAmount();

        dto.setOrderId(orderId);
        dto.setProductDTO(productDTO);
        dto.setAmount(amount);

        return dto;
    }

    public List<OrderItemDTO> toDtos(List<OrderItem> orderItems) {
        return orderItems.stream().map(this::toDto).toList();
    }
}