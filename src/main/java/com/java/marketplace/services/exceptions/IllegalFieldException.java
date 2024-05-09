package com.java.marketplace.services.exceptions;

public class IllegalFieldException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public IllegalFieldException(String msg) {
		super(msg);
	}
}
