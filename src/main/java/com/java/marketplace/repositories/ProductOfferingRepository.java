package com.java.marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.marketplace.entities.ProductOffering;

public interface ProductOfferingRepository extends JpaRepository<ProductOffering, Long>{
	
}
