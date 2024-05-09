package com.java.marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.marketplace.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
