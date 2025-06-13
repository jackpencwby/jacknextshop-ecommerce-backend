package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartListDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CartKey;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CartRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public Cart findById(Long userId, Long productId){
        CartKey cartKey = new CartKey(userId, productId);
        return cartRepository.findById(cartKey).orElseThrow(() -> new ResourceNotFoundException("Cart Not found"));
    }

    public CartListDTO getUserCart(Long userId){
        List<Cart> carts = cartRepository.findAllByUserUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User don't have cart yet"));
        
        CartListDTO userCart = new CartListDTO();
        userCart.setUserId(userId);
        userCart.setCarts(toDtos(carts));

        List<CartDTO> cartDtos = toDtos(carts);

        BigDecimal totalPrice = cartDtos.stream()
                                    .map(dto -> dto.getPrice().multiply(BigDecimal.valueOf(dto.getAmount())))
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        userCart.setTotalPrice(totalPrice);

        return userCart;
    }

    @Transactional
    public Cart createCart(Long userId, Long productId, int amount) {
        User user = userService.findById(userId);
        Product product = productService.findById(productId);

        Optional<Cart> cartInDB = cartRepository.findByUserUserIdAndProductProductId(userId, productId);

        if (cartInDB.isPresent()) {
            cartRepository.deleteByUserUserIdAndProductProductId(userId, productId);
        }

        Cart cart = new Cart();
        CartKey cartKey = new CartKey(userId, productId);
        cart.setId(cartKey);
        cart.setUser(user);
        cart.setProduct(product);
        cart.setAmount(amount);

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart deleteCart(Long userId, Long productId) {
        Cart cart = findById(userId, productId);

        cartRepository.deleteById(cart.getId());

        return cart;
    }

    public Cart updateCart(Long userId, Long productId, int amount) {
        Cart cart = findById(userId, productId);
        
        cart.setAmount(cart.getAmount() + amount);

        return cartRepository.save(cart);
    }

    public CartDTO toDto(Cart cart){
        CartDTO dto = new CartDTO();

        dto.setProductId(cart.getProduct().getProductId());
        dto.setName(cart.getProduct().getName());
        dto.setPrice(cart.getProduct().getPrice());
        dto.setImage(cart.getProduct().getImage());
        dto.setAmount(cart.getAmount());

        return dto;
    }

    public List<CartDTO> toDtos(List<Cart> carts){
        List<CartDTO> dtos = carts.stream().map(c -> toDto(c)).toList();

        return dtos;
    }
}