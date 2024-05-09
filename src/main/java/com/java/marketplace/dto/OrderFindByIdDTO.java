package com.java.marketplace.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.marketplace.entities.Address;
import com.java.marketplace.entities.Order;
import com.java.marketplace.entities.enums.OrderStatus;

public class OrderFindByIdDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",timezone = "GMT")
	private Instant date;
	private OrderStatus orderStatus;
	private Double total;
	
	private CustomerDTO customer;
	
	private Set<OrderItemDTO> productOfferings;
	
	private AddressDTO deliveryAddress;
		
	public OrderFindByIdDTO() {
		
	}
	
	public OrderFindByIdDTO(Order obj) {
		this.id = obj.getId();
		this.date = obj.getDate();
		this.orderStatus = obj.getOrderStatus();
		this.total = obj.getTotal();
		this.customer = new CustomerDTO(obj.getCustomer());
		this.productOfferings = obj.getProductOfferings().stream().map(x -> new OrderItemDTO(x)).collect(Collectors.toSet());
		setDeliveryAddress(obj.getDeliveryAddress());
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

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public Set<OrderItemDTO> getProductOfferings() {
		return productOfferings;
	}

	public AddressDTO getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		if (deliveryAddress != null) {
			this.deliveryAddress = new AddressDTO(deliveryAddress);
		}
	}	
}
