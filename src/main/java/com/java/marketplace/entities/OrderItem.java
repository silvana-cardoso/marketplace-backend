package com.java.marketplace.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.java.marketplace.entities.pk.OrderItemPK;

@Entity
@Table(name = "tb_order_product_offering")
@SQLDelete(sql = "UPDATE tb_order_product_offering SET deleted = true WHERE ORDER_ID = ? and PRODUCT_ID = ?")
@Where(clause = "deleted=false")
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private OrderItemPK id = new OrderItemPK();
	
	private Double discount;
	private Integer quantity;
	private Double totalPriceItem;
	
	private Boolean deleted = Boolean.FALSE;
	
	public OrderItem() {
		
	}

	public OrderItem(Order order, ProductOffering productOffering, Double discount, Integer quantity) {
		id.setOrder(order);
		id.setProductOffering(productOffering);
		this.discount = discount;
		this.quantity = quantity;
		setTotalPriceItem();
	}
	
	public OrderItemPK getId() {
		return id;
	}
	
	public Order getOrder() {
		return id.getOrder();
	}

	public void setOrder(Order order) {
		 id.setOrder(order);
	}
	
	public ProductOffering getProductOffering() {
		return id.getProductOffering();
	}

	public void setProductOffering(ProductOffering productOffering) {
		 id.setProductOffering(productOffering);
	}
	
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalPriceItem() {
		return totalPriceItem;
	}

	public void setTotalPriceItem() {
		Double subTotalItem = getProductOffering().getUnitPrice() * quantity;
		if (discount != null) {
			this.totalPriceItem = subTotalItem - discount * subTotalItem;
		} else {
			this.totalPriceItem = subTotalItem;
		}
	}
	
	public Boolean getDeleted() {
		return deleted;
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
		OrderItem other = (OrderItem) obj;
		return Objects.equals(id, other.id);
	}
}
