package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.user.UserRequestBodyDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.user.UserResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.UserRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CloudinaryService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping()
    public ResponseEntity<APIResponseDTO<?>> getUserInfo(OAuth2AuthenticationToken token) {
        User user = userService.getUserByToken(token);
        APIResponseDTO<UserResponseDTO> res = new APIResponseDTO<>();
        res.setMessage("Success");
        res.setData(userService.toDto(user));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping()
    public ResponseEntity<APIResponseDTO<?>> updateUserInfo(OAuth2AuthenticationToken token,
            @ModelAttribute UserRequestBodyDTO userRequest) {
        User user = userService.getUserByToken(token);
        if (userRequest.getFname() != null) {
            user.setFname(userRequest.getFname());
        }
        if (userRequest.getLname() != null) {
            user.setLname(userRequest.getLname());
        }
        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getBirthdate() != null) {
            user.setBirthdate(userRequest.getBirthdate());
        }
        if (userRequest.getImage() != null) {
            String imageUrl = cloudinaryService.uploadImage(userRequest.getImage());
            user.setImage(imageUrl);
        }

        userRepository.save(user);

        APIResponseDTO<UserResponseDTO> res = new APIResponseDTO<>();
        res.setMessage("User Update Success");
        res.setData(userService.toDto(user));
        
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}