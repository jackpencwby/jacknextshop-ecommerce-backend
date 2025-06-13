package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    public ResponseEntity<?> getUserOrder(OAuth2AuthenticationToken token) {
        User user = userService.getUserByToken(token);

        List<Order> orders = orderService.getUserOrder(user.getUserId());

        List<OrderDTO> orderDTOs = orderService.toDtos(orders);

        APIResponseDTO<List<OrderDTO>> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(orderDTOs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getUserOrderDetail(
        OAuth2AuthenticationToken token,
        @PathVariable Long orderId
    ) 
    {
        User user = userService.getUserByToken(token);

        Order order = orderService.findById(orderId);

        if(!order.getUser().getUserId().equals(user.getUserId())){
            return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).build();
        }

        OrderDTO orderDTO = orderService.toDto(order);

        APIResponseDTO<OrderDTO> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(orderDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<?> createOrder(OAuth2AuthenticationToken token,
            @Valid @RequestBody CreateOrderDTO createOrderDTO) {
        User user = userService.getUserByToken(token);

        Order order = orderService.createOrder(
                user.getUserId(),
                createOrderDTO.getAddressId()
        );

        OrderDTO orderDTO = orderService.toDto(order);

        APIResponseDTO<OrderDTO> response = new APIResponseDTO<>();
        response.setMessage("Created Order successfully.");
        response.setData(orderDTO);

        return ResponseEntity.ok(response);
    }
}
