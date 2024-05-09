package com.java.marketplace.dto;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.marketplace.entities.Order;
import com.java.marketplace.entities.enums.OrderStatus;

public class OrderDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",timezone = "GMT")
	private Instant date;
	private OrderStatus orderStatus;
	private Double total;
		
	public OrderDTO() {
		
	}
	
	public OrderDTO(Order obj) {
		this.id = obj.getId();
		this.date = obj.getDate();
		this.orderStatus = obj.getOrderStatus();
		this.total = obj.getTotal();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}
