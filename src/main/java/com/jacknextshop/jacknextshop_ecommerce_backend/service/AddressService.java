package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.address.AddressDTO;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Address;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.User;
import com.jacknextshop.jacknextshop_ecommerce_backend.exception.ResourceNotFoundException;
import com.jacknextshop.jacknextshop_ecommerce_backend.repository.AddressRepository;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    public Address findByAddressId(UUID addressId) {
        return addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found."));
    }

    public List<Address> getAddress(OAuth2User principal) {
        User user = userService.getCurrentUser(principal);

        return addressRepository.findByUserUserId(user.getUserId());
    }

    public Address createAddress(
            OAuth2User principal,
            String receiveFname,
            String receiveLname,
            String addressLine,
            String city,
            String province,
            String postalCode,
            String phoneNumber) {

        User user = userService.getCurrentUser(principal);

        Address address = new Address();

        address.setUser(user);
        address.setReceiveFname(receiveFname);
        address.setReceiveLname(receiveLname);
        address.setAddressLine(addressLine);
        address.setCity(city);
        address.setProvince(province);
        address.setPostalCode(postalCode);
        address.setPhoneNumber(phoneNumber);

        List<Address> AllAddressOfUser = addressRepository.findByUserUserId(user.getUserId());
        if (AllAddressOfUser.isEmpty()) {
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }

        return addressRepository.save(address);
    }

    public AddressDTO toDto(Address address) {
        AddressDTO dto = new AddressDTO();

        dto.setReceiveFname(address.getReceiveFname());
        dto.setReceiveLname(address.getReceiveLname());
        dto.setAddressLine(address.getAddressLine());
        dto.setCity(address.getCity());
        dto.setProvince(address.getProvince());
        dto.setPostalCode(address.getPostalCode());
        dto.setPhoneNumber(address.getPhoneNumber());

        return dto;
    }

    public List<AddressDTO> toDtos(List<Address> addresses) {
        return addresses.stream().map(this::toDto).toList();
    }
}