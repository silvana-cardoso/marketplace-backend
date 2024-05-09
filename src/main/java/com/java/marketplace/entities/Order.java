package com.java.marketplace.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.java.marketplace.entities.enums.OrderStatus;

@Entity
@Table(name="tb_order")
@SQLDelete(sql = "UPDATE tb_order SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Order implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Instant date;
	private Integer orderStatus;
	private Double total;
	
//	soft delete
	private Boolean deleted = Boolean.FALSE;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "id.order", cascade = CascadeType.REMOVE)
	private Set<OrderItem> productOfferings = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address deliveryAddress;
	
	public Order() {
		
	}

	public Order(Long id, Instant date, Customer customer) {
		this.id = id;
		this.date = date;
		this.customer = customer;
		setOrderStatus(OrderStatus.OPEN);
		getTotal();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}
	
	public OrderStatus getOrderStatus() {
		return OrderStatus.valueOf(orderStatus);
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		if (orderStatus != null) {
			this.orderStatus = orderStatus.getCode();
		}
	}
	
	public Double getTotal() {
		Double total = 0.0;
		for (OrderItem x : getProductOfferings()) {
			total += x.getTotalPriceItem();
		}
		this.total = total;
		return total;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Set<OrderItem> getProductOfferings() {
		return productOfferings;
	}
	
	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
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
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}
}
