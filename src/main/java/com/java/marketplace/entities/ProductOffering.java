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

import com.java.marketplace.entities.enums.POState;

@Entity
@Table(name = "tb_product_offering")
@SQLDelete(sql = "UPDATE tb_product_offering SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class ProductOffering implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String productName;
	private Double unitPrice;
	private Boolean sellIndicator;
	private Integer stock;
	private Integer state;
	
//	soft delete
	private Boolean deleted = Boolean.FALSE;
	
	@OneToMany(mappedBy = "id.productOffering")
	private Set<OrderItem> productOfferings = new HashSet<>();
	
	public ProductOffering() {
		
	}
	
	public ProductOffering(Long id, String productName, Double unitPrice, Boolean sellIndicator, Integer stock, POState state) {
		this.id = id;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.sellIndicator = sellIndicator;
		this.stock = stock;
		setState(state);
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
	
//	@PrePersist
//    @PreUpdate
//	public void setSellIndicator() {
//		if (stock <= 0) {
//			this.sellIndicator = false;
//		}
//	}
	
	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public POState getState() {
		return POState.valueOf(state);
	}

	public void setState(POState state) {
		if (state != null) {
			this.state = state.getCode();
		}
	}
	
	public Boolean getDeleted() {
		return deleted;
	}
	
	public Set<OrderItem> getorderItems() {
		return productOfferings;
	}
	
	public Set<Order> getOrders(){
		Set<Order> set = new HashSet<>();
		for (OrderItem x : productOfferings) {
			set.add(x.getOrder());
		}
		return set;
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
		ProductOffering other = (ProductOffering) obj;
		return Objects.equals(id, other.id);
	}

}
