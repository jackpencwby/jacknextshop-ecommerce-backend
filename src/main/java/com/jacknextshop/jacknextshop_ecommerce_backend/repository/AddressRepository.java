package com.jacknextshop.jacknextshop_ecommerce_backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByAddressId(UUID addressId);
    List<Address> findByUserUserId(UUID userId);
}