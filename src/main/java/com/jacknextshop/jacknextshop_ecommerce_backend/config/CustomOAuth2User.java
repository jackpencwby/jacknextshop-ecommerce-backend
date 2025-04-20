package com.jacknextshop.jacknextshop_ecommerce_backend.config;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;

public class CustomOAuth2User extends CustomUserDetails implements OAuth2User {

    private final Map<String, Object> attributes;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        super(user);
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
 
    @Override
    public String getName() {
        return getUser().getProvider()+getUser().getProviderId();
    }
}
