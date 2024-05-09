package com.java.marketplace.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import com.java.marketplace.entities.ProductOffering;
import com.java.marketplace.entities.enums.POState;

public class ProductOfferingFindByIdDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String productName;
	private Double unitPrice;
	private Boolean sellIndicator;
	private Integer stock;
	private POState state;
	private Set<OrderFindByIdDTO> orders;

	
	public ProductOfferingFindByIdDTO() {
		
	}

	public ProductOfferingFindByIdDTO(ProductOffering obj) {
		this.id = obj.getId();
		this.productName = obj.getProductName();
		this.unitPrice = obj.getUnitPrice();
		this.sellIndicator = obj.getSellIndicator();
		this.stock = obj.getStock();
		this.state = obj.getState();
		this.orders = obj.getOrders().stream().map(x -> new OrderFindByIdDTO(x)).collect(Collectors.toSet());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Boolean getSellIndicator() {
		return sellIndicator;
	}

	public void setSellIndicator(Boolean sellIndicator) {
		this.sellIndicator = sellIndicator;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public POState getState() {
		return state;
	}

	public void setState(POState state) {
		this.state = state;
	}
	
	public Set<OrderFindByIdDTO> getOrders() {
		return orders;
	}
}
