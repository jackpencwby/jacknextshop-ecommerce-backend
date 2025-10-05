package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.order.CreateOrderDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.order.OrderDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Order;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserForBiddenException;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.OrderService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getOrder(@AuthenticationPrincipal OAuth2User principal) {
        List<Order> orders = orderService.getOrder(principal);

        List<OrderDTO> orderDTOs = orderService.toDtos(orders);

        APIResponseDTO<List<OrderDTO>> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(orderDTOs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetail(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable UUID orderId) {

        User user = userService.getCurrentUser(principal);
        Order order = orderService.findByOrderId(orderId);

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new UserForBiddenException("Access denied to this order.");
        }

        OrderDTO orderDTO = orderService.toDto(order);

        APIResponseDTO<OrderDTO> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(orderDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal OAuth2User principal,
            @Valid @RequestBody CreateOrderDTO createOrderDTO) {

        Order order = orderService.createOrder(
                principal,
                createOrderDTO.getAddressId());

        OrderDTO orderDTO = orderService.toDto(order);

        APIResponseDTO<OrderDTO> response = new APIResponseDTO<>();
        response.setMessage("Created Order successfully.");
        response.setData(orderDTO);

        return ResponseEntity.ok(response);
    }
}
