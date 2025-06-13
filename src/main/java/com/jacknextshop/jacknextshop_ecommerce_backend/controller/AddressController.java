package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.address.AddressDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.address.CreateAddressDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Address;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.AddressService;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getUserAddress(OAuth2AuthenticationToken token) {
        User user = userService.getUserByToken(token);

        List<Address> addresses = addressService.getUserAddress(user.getUserId());

        List<AddressDTO> addressDTOs = addressService.toDtos(addresses);

        APIResponseDTO<List<AddressDTO>> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(addressDTOs);

        return ResponseEntity.ok(response);
    }
    
    @PostMapping()
    public ResponseEntity<?> createAddress(OAuth2AuthenticationToken token, 
    @Valid @RequestBody CreateAddressDTO createAddressDTO
    ) {
        User user = userService.getUserByToken(token);

        Address address = addressService.createAddress(
            user.getUserId(),
            createAddressDTO.getReceiveFname(),
            createAddressDTO.getReceiveLname(),
            createAddressDTO.getAddressLine(),
            createAddressDTO.getCity(),
            createAddressDTO.getProvince(),
            createAddressDTO.getPostalCode(),
            createAddressDTO.getPhoneNumber()
        );

        AddressDTO addressDTO = addressService.toDto(address);

        APIResponseDTO<AddressDTO> response = new APIResponseDTO<>();
        response.setMessage("Created address successfully.");
        response.setData(addressDTO);

        return ResponseEntity.ok(response);
    }

}
