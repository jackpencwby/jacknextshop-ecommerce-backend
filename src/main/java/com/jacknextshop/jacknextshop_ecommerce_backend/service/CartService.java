package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDto;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CartRepository;

@Service
public class CartService {
    @Autowired 
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public Cart UserAddProductToCart(Long userId, Long productId, int amount, OAuth2AuthenticationToken token){
        
        User user = userService.getUserByToken(token);
        Product product = productService.findById(productId);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setAmount(amount);

        return cartRepository.save(cart);
    }

    // public CartDTO toDto(Cart cart){
    //     CartDTO dto = new CartDTO();
        

    //     dto.setProductDto(cart.g);
    //     dto.setUserId

    //     return dto;
    // }

    // public List<ProductDto> toDtos(List<Product> products){
    //     List<ProductDto> dtos = products.stream().map(p -> toDto(p)).toList();

    //     return dtos;
    // }

    
}
