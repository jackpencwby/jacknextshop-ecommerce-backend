package com.jacknextshop.jacknextshop_ecommerce_backend.exception.user;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
