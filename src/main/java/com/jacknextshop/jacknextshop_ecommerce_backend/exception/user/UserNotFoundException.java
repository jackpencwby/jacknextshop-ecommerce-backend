package com.jacknextshop.jacknextshop_ecommerce_backend.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
