package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.config.CustomOAuth2User;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.UserRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomOAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = oAuth2User.getAttribute("email");
        String fname = null;
        String lname = null;
        String image = null;
        String providerId = null;
        LocalDate birthdate = null;
        
        // Check Provider
        if("google".equals(provider)){
            fname = oAuth2User.getAttribute("given_name");
            lname = oAuth2User.getAttribute("family_name");
            image = oAuth2User.getAttribute("picture");
            providerId = oAuth2User.getAttribute("sub");
        // }else if("facebook".equals(provider)){
        //     fname = oAuth2User.getAttribute("name");
        //     lname = "";
        //     providerId = oAuth2User.getAttribute("id");
        //     Map<String, Object> pic = oAuth2User.getAttribute("picture");
        //     if (pic != null) {
        //         Map<String, Object> data = (Map <String, Object>) pic.get("data");
        //         image = (String) data.get("url"); 
        //         }
        // }else if("github".equals(provider)){
        //     fname = oAuth2User.getAttribute("name");
        //     lname = "";
        //     image = oAuth2User.getAttribute("avatar_url");
        //     Object idAttr = oAuth2User.getAttribute("id");
        //     if (idAttr != null) {
        //         providerId = idAttr.toString();
        //     } else {
        //         throw new IllegalArgumentException("GitHub ID is missing in OAuth2 attributes");
        //     }

        }else{
            throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + provider);
        }

        // Saving on Database
        var user = userRepository.findByProviderAndProviderId(provider, providerId);
        Boolean isExistUser = user.isPresent();
        if(!isExistUser){
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFname(fname);
            newUser.setLname(lname);
            newUser.setImage(image);
            newUser.setBirthdate(birthdate);
            newUser.setProvider(provider);
            newUser.setProviderId(providerId);
            newUser.setIsAdmin(false);
            User savedUser = userRepository.save(newUser);
            
            return new CustomOAuth2User(savedUser, oAuth2User.getAttributes());
        }

        return new CustomOAuth2User(user.get(), oAuth2User.getAttributes());
    }
}