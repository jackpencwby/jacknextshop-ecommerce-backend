package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartItemDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CreateCartItemDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.CartItem;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CartService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping()
    public ResponseEntity<?> getCart(@AuthenticationPrincipal OAuth2User principal) {
        CartDTO cartDTO = cartService.getCart(principal);

        APIResponseDTO<CartDTO> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(cartDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<?> createCart(@AuthenticationPrincipal OAuth2User principal,
            @Valid @RequestBody CreateCartItemDTO createCartItemDTO) {

        CartItem cartItem = cartService.createCartItem(
                principal,
                createCartItemDTO.getProductId(),
                createCartItemDTO.getAmount());

        CartItemDTO cartItemDTO = cartService.toDto(cartItem);

        APIResponseDTO<CartItemDTO> response = new APIResponseDTO<>();
        response.setMessage("Created cart successfully.");
        response.setData(cartItemDTO);

        return ResponseEntity.ok(response);
    }

}
