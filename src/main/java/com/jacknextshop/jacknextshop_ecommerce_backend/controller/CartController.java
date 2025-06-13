package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartListDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CreateCartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.UpdateCartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CartService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getUserCart(OAuth2AuthenticationToken token) {
        User user = userService.getUserByToken(token);

        CartListDTO cartUser = cartService.getUserCart(user.getUserId());

        APIResponseDTO<CartListDTO> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(cartUser);

        return ResponseEntity.ok(response);
    }
    
    @PostMapping()
    public ResponseEntity<?> createCart(OAuth2AuthenticationToken token, 
    @Valid @RequestBody CreateCartDTO createCartDTO
    ) {
        User user = userService.getUserByToken(token);

        Cart cart = cartService.createCart(
            user.getUserId(), 
            createCartDTO.getProductId(), 
            createCartDTO.getAmount()
        );

        CartDTO cartDTO = cartService.toDto(cart);

        APIResponseDTO<CartDTO> response = new APIResponseDTO<>();
        response.setMessage("Created cart successfully.");
        response.setData(cartDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateCart(OAuth2AuthenticationToken token, 
    @Valid @RequestBody UpdateCartDTO updateCartDTO,
    @PathVariable Long productId
    ) {
        User user = userService.getUserByToken(token);

        Cart cart = cartService.updateCart(
            user.getUserId(), 
            productId,
            updateCartDTO.getAmount()
        );

        CartDTO cartDTO = cartService.toDto(cart);

        APIResponseDTO<CartDTO> response = new APIResponseDTO<>();
        response.setMessage("Updated cart successfully.");
        response.setData(cartDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteCart(OAuth2AuthenticationToken token, 
    @PathVariable Long productId
    ) {
        User user = userService.getUserByToken(token);

        Cart cart = cartService.deleteCart(
            user.getUserId(), 
            productId
        );

        CartDTO cartDTO = cartService.toDto(cart);

        APIResponseDTO<CartDTO> response = new APIResponseDTO<>();
        response.setMessage("Deleted cart successfully.");
        response.setData(cartDTO);

        return ResponseEntity.ok(response);
    }
}
