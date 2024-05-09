package com.java.marketplace.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.java.marketplace.entities.ProductOffering;
import com.java.marketplace.entities.enums.POState;

public class ProductOfferingDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	@NotBlank(message = "Product name is mandatory")
	private String productName;
	@NotNull(message = "Unit price is mandatory")
	private Double unitPrice;
	@NotNull(message = "Sell indicator is mandatory")
	private Boolean sellIndicator;
	@NotNull(message = "Stock is mandatory")
	private Integer stock;
	@NotNull(message = "State is mandatory")
	private POState state;
		
	public ProductOfferingDTO() {
		
	}

	public ProductOfferingDTO(ProductOffering obj) {
		this.id = obj.getId();
		this.productName = obj.getProductName();
		this.unitPrice = obj.getUnitPrice();
		this.sellIndicator = obj.getSellIndicator();
		this.stock = obj.getStock();
		this.state = obj.getState();
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
}
