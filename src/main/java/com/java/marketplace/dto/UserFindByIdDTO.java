package com.java.marketplace.dto;

import java.io.Serializable;

import com.java.marketplace.entities.User;

public class UserFindByIdDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String email;
	private CustomerDTO customer;
	private RoleDTO role;
	
	public UserFindByIdDTO() {
		
	}
	
	public UserFindByIdDTO(User obj) {
		id = obj.getId();
		email = obj.getEmail();
		if (obj.getCustomer() != null) {
			customer = new CustomerDTO(obj.getCustomer());
		}
		role = new RoleDTO(obj.getRole());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public RoleDTO getRole() {
		return role;
	}

	public void setRole(RoleDTO role) {
		this.role = role;
	}
}
