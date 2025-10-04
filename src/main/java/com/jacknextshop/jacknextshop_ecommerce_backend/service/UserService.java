package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
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
}
