package com.java.marketplace.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.java.marketplace.entities.enums.CustomerType;

/**
 * Represents a customer entity in the marketplace system.
 *
 * @author Silvana Cardoso
 *
 */
@Entity
@Table(name = "tb_customer", uniqueConstraints = @UniqueConstraint(columnNames = { "deleted", "documentNumber" }))
@SQLDelete(sql = "UPDATE tb_customer SET deleted = null WHERE id=?")
@Where(clause = "deleted=false")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The unique identifier of the customer. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The name of the customer. */
	private String customerName;

	/** The document number of the customer. */
	private Long documentNumber;

	/** The status of the customer. */
	private String customerStatus;

	/** The type of the customer. */
	private Integer customerType;

	/** The credit score of the customer. */
	private String creditScore;

	/** The system password of the customer. */
	private String password;

	/** Field to perform soft delete */
	private Boolean deleted = Boolean.FALSE;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
	private List<Order> orders = new ArrayList<>();

	@OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
	private Set<Address> addresses = new HashSet<>();

	public Customer() {

	}

	public Customer(Long id, String customerName, Long documentNumber, String customerStatus, CustomerType customerType,
			String creditScore, String password, User user) {
		this.id = id;
		this.customerName = customerName;
		this.documentNumber = documentNumber;
		this.customerStatus = customerStatus;
		setCustomerType(customerType);
		this.creditScore = creditScore;
		this.password = password;
		this.user = user;
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
		return CustomerType.valueOf(customerType);
	}

	public void setCustomerType(CustomerType customerType) {
		if (customerType != null) {
			this.customerType = customerType.getCode();
		}
	}

	public String getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(String creditScore) {
		this.creditScore = creditScore;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public Set<Address> getAddresses() {
		return addresses;
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
		Customer other = (Customer) obj;
		return Objects.equals(id, other.id);
	}
}
