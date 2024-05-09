package com.java.marketplace.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.marketplace.entities.enums.Authorities;

@Entity
@Table(name = "tb_role")
@SQLDelete(sql = "UPDATE tb_role SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer authority;
	
//	soft delete
	private Boolean deleted = Boolean.FALSE;
	
	@JsonIgnore
	@OneToMany(mappedBy = "role")
	private Set<User> users = new HashSet<>();
	
	public Role() {
		
	}

	public Role(Long id, Authorities authority) {
		super();
		this.id = id;
		setAuthority(authority);;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Authorities getAuthority() {
		return Authorities.valueOf(authority);
	}

	public void setAuthority(Authorities authority) {
		if (authority != null) {
			this.authority = authority.getCode();
		}
	}
	
	public boolean getDeleted() {
		return deleted;
	}

	public Set<User> getUsers() {
		return users;
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
		Role other = (Role) obj;
		return Objects.equals(id, other.id);
	}
}
