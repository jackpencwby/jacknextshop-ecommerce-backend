package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.user.UserResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserIsNotAdminException;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.user.UserNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
    }

    public User getUserByToken(OAuth2AuthenticationToken token) {
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

        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user;
    }

    public void checkAdmin(OAuth2AuthenticationToken token) {
        User user = this.getUserByToken(token);
        if (!user.getIsAdmin()) {
            throw new UserIsNotAdminException("You do not have administrative privileges to perform this action.");
        }
    }

    public UserResponseDTO toDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setFname(user.getFname());
        dto.setLname(user.getLname());
        dto.setEmail(user.getEmail());
        dto.setBirthDate(user.getBirthdate());
        dto.setImage(user.getImage());
        return dto;
    }
}
