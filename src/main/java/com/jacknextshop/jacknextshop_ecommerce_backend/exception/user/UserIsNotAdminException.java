package com.jacknextshop.jacknextshop_ecommerce_backend.exception.user;

public class UserIsNotAdminException extends RuntimeException {
    public UserIsNotAdminException(String message) {
        super(message);
    }
}
