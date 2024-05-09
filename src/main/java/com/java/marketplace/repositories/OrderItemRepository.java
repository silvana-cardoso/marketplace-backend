package com.java.marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.marketplace.entities.OrderItem;
import com.java.marketplace.entities.pk.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK>{

}
