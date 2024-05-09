package com.java.marketplace.dto;

import java.io.Serializable;
import java.time.Instant;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.marketplace.entities.enums.OrderStatus;

public class OrderInsertDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	@NotNull(message = "Customer id is mandatory")
	private Long customerId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",timezone = "GMT")
	private Instant date;
	private OrderStatus orderStatus;
	private Double total;
		
	public OrderInsertDTO() {
		
	}
	
	public OrderInsertDTO(Long customerId, Instant date) {
		this.customerId = customerId;
		this.date = date;
		this.orderStatus = OrderStatus.OPEN;
		this.total = 0.0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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
