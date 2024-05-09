package com.java.marketplace.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.java.marketplace.entities.OrderItem;

public class OrderItemDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private ProductOfferingDTO productOffering;
	
	private Double discount;
	@NotNull
	private Integer quantity;
	private Double totalPriceItem;

	public OrderItemDTO() {

	}

	public OrderItemDTO(OrderItem orderItem) {
		this.productOffering = new ProductOfferingDTO(orderItem.getProductOffering());
		this.discount = orderItem.getDiscount();
		this.quantity = orderItem.getQuantity();
		this.totalPriceItem = orderItem.getTotalPriceItem();
	}

	public ProductOfferingDTO getProductOffering() {
		return productOffering;
	}

	public void setProductOffering(ProductOfferingDTO item) {
		this.productOffering = item;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalPriceItem() {
		return totalPriceItem;
	}

	public void setTotalPriceItem(Double totalPriceItem) {
		this.totalPriceItem = totalPriceItem;
	}
}