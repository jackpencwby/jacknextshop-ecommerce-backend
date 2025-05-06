package com.jacknextshop.jacknextshop_ecommerce_backend.service;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.ResponseCartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductCartDto;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.key.CartKey;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
=======
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDto;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Cart;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
>>>>>>> main
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CartRepository;

@Service
public class CartService {
<<<<<<< HEAD
    
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> findAllByUserId(Long userId) {
        return cartRepository.findAllByIdUserId(userId);
    }
    
    public ResponseCartDTO toDtos(List<Cart> carts) {
        ResponseCartDTO dto = new ResponseCartDTO();
        if(carts.size() == 0) {
            throw new ResourceNotFoundException("Cart is not found");
        }
        dto.setFname(carts.get(0).getUser().getFname());
        dto.setLname(carts.get(0).getUser().getLname());
        dto.setUserId(carts.get(0).getUser().getUserId());
        List<ProductCartDto> products = new ArrayList<>();
        for(Cart c: carts) {
            ProductCartDto p = new ProductCartDto();
            p.setAmount(c.getAmount());
            p.setImage(c.getProduct().getImage());
            p.setName(c.getProduct().getName());
            p.setPrice(c.getProduct().getPrice());
            p.setProductId(c.getProduct().getProductId());
            products.add(p);
        }
        dto.setProducts(products);
        return dto;
    }
    
    public Cart findById(Long userId, Long productId) {
        CartKey key = new CartKey();
        key.setProductId(productId);
        key.setUserId(userId);
        Optional<Cart> cart = cartRepository.findById(key);
        if(! cart.isPresent()) {
            throw new ResourceNotFoundException("Not found Product in this user's cart.");
        }
        return cart.get();
    }
=======
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

    
>>>>>>> main
}
