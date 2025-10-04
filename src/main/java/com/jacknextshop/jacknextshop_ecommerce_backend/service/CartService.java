package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart.CartItemDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.product.ProductDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.CartItem;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Product;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.CartItemRepository;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public CartDTO getCart(OAuth2User principal) {
        User user = userService.getCurrentUser(principal);

        List<CartItem> cartItems = cartItemRepository.findByUserUserId(user.getUserId());

        List<CartItemDTO> cartItemDTOs = toDtos(cartItems);

        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(user.getUserId());
        cartDTO.setCartItemDTOs(cartItemDTOs);

        BigDecimal totalPrice = cartItemDTOs.stream()
                .map(dto -> dto.getProductDTO().getPrice().multiply(BigDecimal.valueOf(dto.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cartDTO.setTotalPrice(totalPrice);

        return cartDTO;
    }

    public CartItem createCartItem(OAuth2User principal, UUID productId, Integer amount) {
        User user = userService.getCurrentUser(principal);

        Product product = productService.findByProductId(productId);

        Optional<CartItem> cartItemInDB = cartItemRepository.findByUserUserIdAndProductProductId(user.getUserId(),
                productId);

        if (cartItemInDB.isPresent()) {
            cartItemRepository.deleteByUserUserIdAndProductProductId(user.getUserId(), productId);
        }

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setAmount(amount);

        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(OAuth2User principal, UUID productId) {
        User user = userService.getCurrentUser(principal);

        Optional<CartItem> cartItemInDB = cartItemRepository.findByUserUserIdAndProductProductId(user.getUserId(),
                productId);

        if (cartItemInDB.isPresent()) {
            cartItemRepository.deleteByUserUserIdAndProductProductId(user.getUserId(), productId);
        } else {
            throw new ResourceNotFoundException("Cart item not found.");
        }
    }

    public CartItemDTO toDto(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();

        Product product = productService.findByProductId(cartItem.getProduct().getProductId());
        ProductDTO productDTO = productService.toDto(product);

        dto.setProductDTO(productDTO);
        dto.setAmount(cartItem.getAmount());

        return dto;
    }

    public List<CartItemDTO> toDtos(List<CartItem> cartItems) {
        return cartItems.stream().map(c -> toDto(c)).toList();
    }
}