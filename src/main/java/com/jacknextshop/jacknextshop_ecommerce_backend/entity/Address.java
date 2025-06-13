package com.jacknextshop.jacknextshop_ecommerce_backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "address_id")
    private Long addressId;

    // Fields
    @Column(name = "receive_fname")
    private String receiveFname;

    @Column(name = "receive_lname")
    private String receiveLname;

    @Column(name = "address_line")
    private String addressLine;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "is_default")   
    private Boolean isDefault; 
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // Relationship
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

