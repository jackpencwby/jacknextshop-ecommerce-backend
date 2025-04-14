package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.user.UserRequestBodyDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.UserRepository;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.CloudinaryService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("")
    public ResponseEntity<APIResponseDTO<User>> getUserInfo(OAuth2AuthenticationToken token) {
        Optional<User> userOptional = userService.getUserByToken(token);
        if(userOptional.isPresent()){
            APIResponseDTO<User> res = new APIResponseDTO<>();
            res.setMessage("Success");
            res.setData(userOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        APIResponseDTO<User> res = new APIResponseDTO<>();
        res.setMessage("Not found");
        res.setData(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @PutMapping("")
    public ResponseEntity<APIResponseDTO<User>> updateUserInfo(OAuth2AuthenticationToken token, @ModelAttribute UserRequestBodyDTO userRequest){
        Optional<User> userOptional = userService.getUserByToken(token);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(userRequest.getFname() != null){
                user.setFname(userRequest.getFname());             
            }
            if(userRequest.getLname() != null){
                user.setLname(userRequest.getLname());
            }
            if(userRequest.getEmail() != null){
                user.setEmail(userRequest.getEmail());
            }
            if(userRequest.getBirthdate() != null){
                user.setBirthdate(userRequest.getBirthdate());
            }
            
            try{
                if(userRequest.getImage() != null){
                    String imageUrl = cloudinaryService.uploadImage(userRequest.getImage());
                    user.setImage(imageUrl);
                }
            }catch(Exception e){
                APIResponseDTO<User> res = new APIResponseDTO<>();
                res.setMessage("Bad request");
                res.setData(null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            userRepository.save(user);
            APIResponseDTO<User> res = new APIResponseDTO<>();
            res.setMessage("User Update Success");
            res.setData(user);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }

        APIResponseDTO<User> res = new APIResponseDTO<>();
        res.setMessage("Not found");
        res.setData(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    
}
