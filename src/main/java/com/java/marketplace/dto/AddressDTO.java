package com.java.marketplace.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.java.marketplace.entities.Address;
import com.java.marketplace.entities.enums.AddressType;

public class AddressDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	@NotBlank(message = "Street is mandatory")
	private String street;
	@NotNull(message = "House number is mandatory")
	private Integer houseNumber;
	@NotBlank(message = "Neighborhood is mandatory")
	private String neighborhood;
	@NotBlank(message = "Zip code is mandatory")
	private String zipCode;
	@NotBlank(message = "Country is mandatory")
	private String country;
	@NotNull(message = "Address type is mandatory")
	private AddressType addressType;
	
	public AddressDTO() {
		
	}
	
	public AddressDTO(Address obj) {
		this.id = obj.getId();
		this.street = obj.getStreet();
		this.houseNumber = obj.getHouseNumber();
		this.neighborhood = obj.getNeighborhood();
		this.zipCode = obj.getZipCode();
		this.country = obj.getCountry();
		this.addressType = obj.getAddressType();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(Integer houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public AddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}
}
