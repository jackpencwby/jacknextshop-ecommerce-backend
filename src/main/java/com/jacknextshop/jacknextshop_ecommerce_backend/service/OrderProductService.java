package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.orderproduct.OrderProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Order;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.OrderProduct;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.OrderProductKey;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.OrderProductRepository;

@Service
public class OrderProductService {

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductService productService;

    public OrderProduct createOrderProduct(Order order, Product product, int amount) {
        OrderProduct orderProduct = new OrderProduct();

        OrderProductKey orderProductKey = new OrderProductKey(
            order.getOrderId(),
            product.getProductId()
        );
        orderProduct.setOrderProductId(orderProductKey);

        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setAmount(amount);

        return orderProductRepository.save(orderProduct);
    }

    public OrderProductDTO toDto(OrderProduct orderProduct) {
        OrderProductDTO dto = new OrderProductDTO();

        dto.setOrderId(orderProduct.getOrder().getOrderId());
        dto.setProductDto(productService.toDto(orderProduct.getProduct()));
        dto.setAmount(orderProduct.getAmount());

        return dto;
    }

    public List<OrderProductDTO> toDtos(List<OrderProduct> orderProducts){
        List<OrderProductDTO> dtos = orderProducts.stream().map(o -> toDto(o)).toList();

        return dtos;
    }
}
