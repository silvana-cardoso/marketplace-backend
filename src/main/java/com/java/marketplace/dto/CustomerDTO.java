package com.java.marketplace.dto;

import java.io.Serializable;

import com.java.marketplace.entities.Customer;
import com.java.marketplace.entities.enums.CustomerType;

public class CustomerDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String customerName;
	private Long documentNumber;
	private String customerStatus;
	private CustomerType customerType;
	private String creditScore;
	
	public CustomerDTO() {
		
	}
	
	public CustomerDTO(Customer obj) {
		this.id = obj.getId();
		this.customerName = obj.getCustomerName();
		this.documentNumber = obj.getDocumentNumber();
		this.customerStatus = obj.getCustomerStatus();
		this.customerType = obj.getCustomerType();
		this.creditScore = obj.getCreditScore();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public String getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(String creditScore) {
		this.creditScore = creditScore;
	}
}
