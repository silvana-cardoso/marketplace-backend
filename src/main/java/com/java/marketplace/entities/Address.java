package com.java.marketplace.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.java.marketplace.entities.enums.AddressType;

/**
 * Represents a customer's shipping address with details such as street, house
 * number, neighborhood, and postal code.
 */
@Entity
@Table(name = "tb_address", uniqueConstraints = @UniqueConstraint(columnNames = { "deleted", "houseNumber", "zipCode",
		"customer_id" }))
@SQLDelete(sql = "UPDATE tb_address SET deleted = null WHERE id=?")
@Where(clause = "deleted=false")
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	/* The identifier in database. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The street address. */
	private String street;

	/** The house number. */
	private Integer houseNumber;

	/** The neighborhood. */
	private String neighborhood;

	/** The postal code. */
	private String zipCode;

	/** The country. */
	private String country;

	/** The address type. */
	private Integer addressType;

	/** Field to perform soft delete */
	private Boolean deleted = Boolean.FALSE;

	/** The customer to which this address is associated. */
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	/** The orders to which this address is associated as a delivery address. */
	@OneToMany(mappedBy = "deliveryAddress")
	private List<Order> orders = new ArrayList<>();

	/**
	 * Constructs a new Address object.
	 */
	public Address() {

	}

	/**
	 * Constructs a new Address object with the specified details.
	 *
	 * @param id           The database identifier.
	 * @param street       The street address.
	 * @param houseNumber  The house number address.
	 * @param neighborhood The neighborhood address.
	 * @param zipCode      The postal code.
	 * @param country      The country.
	 * @param addressType  The type of address.
	 * @param customer     The customer owner of the Address.
	 */
	public Address(Long id, String street, Integer houseNumber, String neighborhood, String zipCode, String country,
			AddressType addressType, Customer customer) {
		super();
		this.id = id;
		this.street = street;
		this.houseNumber = houseNumber;
		this.neighborhood = neighborhood;
		this.zipCode = zipCode;
		this.country = country;
		setAddressType(addressType);
		this.customer = customer;
	}

	/**
	 * Retrieves the address id.
	 *
	 * @return The address id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the address id
	 *
	 * @param id The address id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Retrieves the street address.
	 *
	 * @return The street address.
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Sets the street address.
	 *
	 * @param street The street address to set.
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Retrieves the house number.
	 *
	 * @return The house number.
	 */
	public Integer getHouseNumber() {
		return houseNumber;
	}

	/**
	 * Sets the house number.
	 *
	 * @param houseNumber The house number to set.
	 */
	public void setHouseNumber(Integer houseNumber) {
		this.houseNumber = houseNumber;
	}

	/**
	 * Retrieves the neighborhood.
	 *
	 * @return The neighborhood.
	 */
	public String getNeighborhood() {
		return neighborhood;
	}

	/**
	 * Sets the neighborhood.
	 *
	 * @param neighborhood The neighborhood to set.
	 */
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	/**
	 * Retrieves the postal code.
	 *
	 * @return The postal code.
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Sets the postal code.
	 *
	 * @param zipCode The postal code to set.
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * Retrieves the country.
	 *
	 * @return The country.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets the country.
	 *
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Retrieves the type of address.
	 *
	 * @return The type of address.
	 */
	public AddressType getAddressType() {
		return AddressType.valueOf(addressType);
	}

	/**
	 * Sets the type of address.
	 *
	 * @param addressType The type of address to set.
	 */
	public void setAddressType(AddressType addressType) {
		if (addressType != null) {
			this.addressType = addressType.getCode();
		}
	}

	/**
	 * Indicates whether the address is marked as deleted.
	 *
	 * @return true if the address is marked as deleted, false otherwise.
	 */
	public boolean getDeleted() {
		return deleted;
	}

	/**
	 * Retrieves the customer to which the address is associated.
	 *
	 * @return The customer.
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * Sets the customer to which the address is associated.
	 *
	 * @param customer The customer to set.
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * Retrieves the list of orders that have this address defined as the delivery
	 * address.
	 *
	 * @return The orders.
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * Returns a hash code value for the address. The hash code is based on the id.
	 *
	 * @return a hash code value for this address.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Indicates whether some other object is "equal to" this one. Two addresses are
	 * considered equal if they have the same id.
	 *
	 * @param obj The reference object with which to compare.
	 * @return true if this address is the same as the obj argument; false
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(id, other.id);
	}
}
