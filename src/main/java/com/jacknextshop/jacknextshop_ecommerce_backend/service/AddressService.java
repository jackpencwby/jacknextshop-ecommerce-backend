package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Address findById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address Not found"));
    }

    public List<Address> getUserAddress(Long userId){
        return addressRepository.findAllByUserUserId(userId);
    }

    public Address createAddress(
        Long userId,
        String receiveFname,
        String receiveLname,
        String addressLine,
        String city,
        String province,
        String postalCode,
        String phoneNumber) 
    {
        Address address = new Address();

        User user = userService.findById(userId);
        address.setUser(user);

        address.setReceiveFname(receiveFname);
        address.setReceiveLname(receiveLname);
        address.setAddressLine(addressLine);
        address.setCity(city);
        address.setProvince(province);
        address.setPostalCode(postalCode);
        address.setPhoneNumber(phoneNumber);

        List<Address> AllAddressOfUser = addressRepository.findAllByUserUserId(userId);
        if(AllAddressOfUser.isEmpty()){
            address.setIsDefault(true);
        } else{
            address.setIsDefault(false);
        }

        return addressRepository.save(address);
    }

    public AddressDTO toDto(Address address){
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

    public List<AddressDTO> toDtos(List<Address> addresses){
        List<AddressDTO> dtos = addresses.stream().map(a -> toDto(a)).toList();

        return dtos;
    }
}