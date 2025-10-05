package com.jacknextshop.jacknextshop_ecommerce_backend.dto.address;

import lombok.Data;

@Data
public class CreateAddressDTO {
    private String receiveFname;
    private String receiveLname;
    private String addressLine;
    private String city;
    private String province;
    private String postalCode;
    private String phoneNumber;
}
