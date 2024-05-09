package com.java.marketplace.services.exceptions;

public class OrderAlreadyClosedException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public OrderAlreadyClosedException() {
		super("The order has already been closed.");
	}
}
