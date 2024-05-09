package com.java.marketplace.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.java.marketplace.entities.enums.CustomerType;

public class CustomerInsertDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "User id is mandatory")
	private Long userId;
	@NotBlank(message = "Customer name is mandatory")
	private String customerName;
	@NotNull(message = "Document number is mandatory")
	private Long documentNumber;
	@NotBlank(message = "Customer status is mandatory")
	private String customerStatus;
	private CustomerType customerType;
	@NotBlank(message = "Credit score type is mandatory")
	private String creditScore;

	private Set<AddressDTO> addresses;

	public CustomerInsertDTO() {
	}

	public CustomerInsertDTO(Long userId, String customerName, Long documentNumber, String customerStatus, CustomerType customerType,
			String creditScore) {
		this.userId = userId;
		this.customerName = customerName;
		this.documentNumber = documentNumber;
		this.customerStatus = customerStatus;
		this.customerType = customerType;
		this.creditScore = creditScore;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Set<AddressDTO> getAddresses() {
		return addresses;
	}
}
