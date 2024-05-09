package com.java.marketplace.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table (name = "tb_user", uniqueConstraints = @UniqueConstraint(columnNames = {"deleted","email"}))
@SQLDelete(sql = "UPDATE tb_user SET deleted = null WHERE id=?")
@Where(clause = "deleted=false")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String password;
	
//	soft delete
	private Boolean deleted = Boolean.FALSE;
	
//	allow for a "zero or one" relationship
//	not sure if mappedBy is well put here
	@OneToOne(optional = true, mappedBy = "user", cascade = CascadeType.REMOVE)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	public User() {
		
	}

	public User(Long id, String email, String password) {
		this.id = id;
		this.email = email;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean getDeleted() {
		return deleted;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return id ==  other.id;
	}	
}
