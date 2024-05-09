package com.java.marketplace.services.exceptions;

public class ProductCurrentlyUnavailableForSaleException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ProductCurrentlyUnavailableForSaleException() {
		super("The product is currently unavailable for sale.");
	}
}
