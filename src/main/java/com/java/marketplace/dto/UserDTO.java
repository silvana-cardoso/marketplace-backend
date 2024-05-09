package com.java.marketplace.dto;

import java.io.Serializable;

import com.java.marketplace.entities.User;

public class UserDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String email;
	private RoleDTO role;
	
	public UserDTO() {
		
	}
	
	public UserDTO(User obj) {
		id = obj.getId();
		email = obj.getEmail();
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

	public RoleDTO getRole() {
		return role;
	}

	public void setRole(RoleDTO role) {
		this.role = role;
	}	
}
