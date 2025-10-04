package com.jacknextshop.jacknextshop_ecommerce_backend.security;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.UserRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final UserService userService;

    public CustomOAuth2UserService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String fname = oAuth2User.getAttribute("given_name");
        String lname = oAuth2User.getAttribute("family_name");

        Optional<User> userOptional = userRepository.findByProviderAndProviderId(registrationId, providerId);

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = userService.createUser(registrationId, providerId, email, fname, lname);
        }

        return oAuth2User;
    }
}
