package com.java.marketplace.services.exceptions;

public class ProductOutOfStockException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ProductOutOfStockException() {
		super("The product does not have the required quantity available in stock.");
	}
}
