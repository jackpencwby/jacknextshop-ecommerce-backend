package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.APIResponseDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.address.AddressDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.dto.address.CreateAddressDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Address;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping()
    public ResponseEntity<?> getAddress(@AuthenticationPrincipal OAuth2User principal) {

        List<Address> addresses = addressService.getAddress(principal);

        List<AddressDTO> addressDTOs = addressService.toDtos(addresses);

        APIResponseDTO<List<AddressDTO>> response = new APIResponseDTO<>();
        response.setMessage(null);
        response.setData(addressDTOs);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<?> createAddress(@AuthenticationPrincipal OAuth2User principal,
            @Valid @RequestBody CreateAddressDTO createAddressDTO) {
        Address address = addressService.createAddress(
                principal,
                createAddressDTO.getReceiveFname(),
                createAddressDTO.getReceiveLname(),
                createAddressDTO.getAddressLine(),
                createAddressDTO.getCity(),
                createAddressDTO.getProvince(),
                createAddressDTO.getPostalCode(),
                createAddressDTO.getPhoneNumber());

        AddressDTO addressDTO = addressService.toDto(address);

        APIResponseDTO<AddressDTO> response = new APIResponseDTO<>();
        response.setMessage("Created address successfully.");
        response.setData(addressDTO);

        return ResponseEntity.ok(response);
    }
}
