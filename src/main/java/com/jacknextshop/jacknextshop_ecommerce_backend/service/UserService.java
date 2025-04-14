package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserIsNotAdminException;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByToken(OAuth2AuthenticationToken token){
        String provider = token.getAuthorizedClientRegistrationId();
        String providerId = "";
        var oAuth2User = token.getPrincipal();

        if ("google".equals(provider)) {
            providerId = oAuth2User.getAttribute("sub");
        } else if ("facebook".equals(provider)) {
            providerId = oAuth2User.getAttribute("id");
        } else if ("github".equals(provider)) {
            Object idAttr = oAuth2User.getAttribute("id");
        if (idAttr != null) {
            providerId = idAttr.toString();
        } else {
            throw new IllegalArgumentException("GitHub ID is missing in OAuth2 attributes");
        }
        }
        User user = userRepository.findByProviderAndProviderId(provider, providerId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user;

    }

    public void checkAdmin(OAuth2AuthenticationToken token){
        User user = this.getUserByToken(token);
        if(! user.getIsAdmin()){
            throw new UserIsNotAdminException("You do not have administrative privileges to perform this action.");
        }
    }
}
