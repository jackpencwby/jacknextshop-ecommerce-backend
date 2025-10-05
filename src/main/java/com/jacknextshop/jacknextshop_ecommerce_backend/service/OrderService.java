package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.address.AddressDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.order.OrderDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.order.OrderItemDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Address;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.CartItem;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Order;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.OrderItem;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Order.OrderStatus;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserForBiddenException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CartItemRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.OrderItemRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemService orderItemService;

    public Order findByOrderId(UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
    }

    public List<Order> getOrder(OAuth2User principal) {
        User user = userService.getCurrentUser(principal);

        return orderRepository.findByUserUserId(user.getUserId());
    }

    public Order createOrder(OAuth2User principal, UUID addressId) {
        User user = userService.getCurrentUser(principal);

        Address address = addressService.findByAddressId(addressId);

        if(!address.getUser().getUserId().equals(user.getUserId())) {
            throw new UserForBiddenException("Access denied to this address.");
        }

        List<CartItem> cartItems = cartItemRepository.findByUserUserId(user.getUserId());

        Order order = new Order();

        order.setUser(user);
        order.setAddress(address);

        BigDecimal totalPrice = cartService.getCart(principal).getTotalPrice();
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            orderItemService.createOrderProduct(
                    savedOrder,
                    cartItem.getProduct(),
                    cartItem.getAmount());
        }

        Order completedOrder = findByOrderId(savedOrder.getOrderId());

        return completedOrder;
    }

    public OrderDTO toDto(Order order) {
        OrderDTO dto = new OrderDTO();

        UUID orderId = order.getOrderId();
        AddressDTO addressDTO = addressService.toDto(order.getAddress());

        List<OrderItem> orderItems = orderItemRepository.findByOrderOrderId(orderId);
        List<OrderItemDTO> orderItemDTOs = orderItemService.toDtos(orderItems);

        BigDecimal totalPrice = order.getTotalPrice();
        OrderStatus status = order.getStatus();
        LocalDate orderDate = order.getOrderDate();
        LocalDate shippingDate = order.getShippingDate();

        dto.setOrderId(orderId);
        dto.setAddressDTO(addressDTO);
        dto.setOrderItemDTOs(orderItemDTOs);
        dto.setTotalPrice(totalPrice);
        dto.setStatus(status);
        dto.setOrderDate(orderDate);
        dto.setShippingDate(shippingDate);

        return dto;
    }

    public List<OrderDTO> toDtos(List<Order> orders) {
        return orders.stream().map(this::toDto).toList();
    }
}
