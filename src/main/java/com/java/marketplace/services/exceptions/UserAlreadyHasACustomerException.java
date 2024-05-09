package com.java.marketplace.services.exceptions;

public class UserAlreadyHasACustomerException extends IllegalStateException{
	private static final long serialVersionUID = 1L;
	
	public UserAlreadyHasACustomerException(String msg) {
		super(msg);
	}
}