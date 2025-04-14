package com.jacknextshop.jacknextshop_ecommerce_backend.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Data
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "providerId"})
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "user_id")
    private Long userId;

    // Relationship
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    // Fields
    @Column(name= "email")
    private String email;

    @Column(name= "provider")
    private String provider;

    @Column(name= "provider_id")
    private String providerId;

    @Column(name= "fname")
    private String fname;

    @Column(name= "lname")
    private String lname;

    @Column(name= "birthdate")
    private LocalDate birthdate;

    @Column(name= "image")
    private String image;

    @Column(name= "is_admin")
    private Boolean isAdmin = false;
}