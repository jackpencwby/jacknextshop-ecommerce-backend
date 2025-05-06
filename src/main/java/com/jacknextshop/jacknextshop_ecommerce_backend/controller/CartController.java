package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.ResponseCartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CartKey;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CartRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CartService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.ProductService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartRepository cartRepository;
    
    @GetMapping()
    public ResponseEntity<?> getCart(OAuth2AuthenticationToken token) {
        User user = userService.getUserByToken(token);
        List<Cart> carts = cartService.findAllByUserId(user.getUserId());
        ResponseCartDTO dto = cartService.toDtos(carts);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/{productId}/{amount}")
    public ResponseEntity<?> addProductToCart(OAuth2AuthenticationToken token, 
    @PathVariable Long productId,
    @PathVariable int amount
    ) {
        User user = userService.getUserByToken(token);
        Product product = productService.findById(productId);
        if(product.getStock() < amount) {
            return ResponseEntity.badRequest()
        .body("Sorry, the requested amount is more than what's available in stock. Only " + product.getStock() + " items are currently available.");
        }
        Cart cart = new Cart();
        cart.setAmount(amount);
        cart.setProduct(product);
        cart.setUser(user);
        CartKey cartKey = new CartKey();
        cartKey.setProductId(productId);
        cartKey.setUserId(user.getUserId());
        cart.setId(cartKey);
        cartRepository.save(cart);
        return ResponseEntity.ok().body(this.getCart(token).getBody());
    }

    @PutMapping("/{productId}/{amount}")
    public ResponseEntity<?> updateProductToCart(OAuth2AuthenticationToken token, 
    @PathVariable Long productId,
    @PathVariable int amount
    ) {
        return this.addProductToCart(token, productId, amount);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProductToCart(OAuth2AuthenticationToken token,
    @PathVariable Long productId
    ) {
        User user = userService.getUserByToken(token);
        Cart cart = cartService.findById(user.getUserId(), productId);
        List<Cart> temp = new ArrayList<>();
        temp.add(cart);
        ResponseCartDTO dto = cartService.toDtos(temp);
        cartRepository.delete(cart);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteAllProductToCart(OAuth2AuthenticationToken token) {
        User user = userService.getUserByToken(token);
        List<Cart> carts = cartService.findAllByUserId(user.getUserId());
        ResponseCartDTO dto = cartService.toDtos(carts);
        cartRepository.deleteAll(carts);
        return ResponseEntity.ok().body(dto);
    }
}
