package com.java.marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.marketplace.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
