package com.java.marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.marketplace.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
