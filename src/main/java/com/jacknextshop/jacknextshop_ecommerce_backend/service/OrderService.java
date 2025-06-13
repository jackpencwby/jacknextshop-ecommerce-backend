package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.order.OrderDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Address;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Order;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CartRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.OrderProductRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderProductService orderProductService;

    public Order findById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order Not found"));
    }

    public List<Order> getUserOrder(Long userId) {
        return orderRepository.findAllByUserUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User don't have order yet"));
    }

    public Order createOrder(Long userId, Long addressId) {
        List<Cart> carts = cartRepository.findAllByUserUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User don't have cart yet"));

        Order order = new Order();

        User user = userService.findById(userId);
        order.setUser(user);

        Address address = addressService.findById(addressId);
        order.setAddress(address);

        BigDecimal totalPrice = cartService.getUserCart(userId).getTotalPrice();
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        for (Cart cart : carts) {
            orderProductService.createOrderProduct(
                savedOrder,
                cart.getProduct(),
                cart.getAmount()
            );
        }

        Order completedOrder = findById(savedOrder.getOrderId());

        return completedOrder;
    }

    public OrderDTO toDto(Order order) {
        OrderDTO dto = new OrderDTO();

        dto.setOrderId(order.getOrderId());
        dto.setAddressDTO(addressService.toDto(order.getAddress()));
        dto.setOrderProductDTOs(orderProductService.toDtos(order.getOrderProducts()));
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderDate(order.getOrderDate());
        dto.setShippingDate(order.getShippingDate());
        dto.setStatus(order.getStatus().name());

        return dto;
    }

    public List<OrderDTO> toDtos(List<Order> orders){
        List<OrderDTO> dtos = orders.stream().map(o -> toDto(o)).toList();

        return dtos;
    }
}
