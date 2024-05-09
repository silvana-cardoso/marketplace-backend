package com.java.marketplace.services.exceptions;

public class OrderStatusException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public OrderStatusException(String msg) {
		super(msg);
	}
}
