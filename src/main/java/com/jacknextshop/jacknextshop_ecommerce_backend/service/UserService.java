package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User.Role;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserNotAuthenticatedException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(String provider, String providerId, String email, String fname, String lname) {
        User user = new User();

        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setEmail(email);
        user.setFname(fname);
        user.setLname(lname);

        return userRepository.save(user);
    }

    public Role getRole(OAuth2User principal) {
        if (principal == null) {
            throw new UserNotAuthenticatedException("User not authenticated.");
        }

        String providerId = principal.getAttribute("sub");

        User user = userRepository.findByProviderAndProviderId("google", providerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return user.getRole();
    }

    public boolean isAdmin(OAuth2User principal) {
        if (getRole(principal).equals(Role.ADMIN)) {
            return true;
        }

        return false;
    }
}
