package com.java.marketplace.services.exceptions;

public class GeneralIllegalStateException extends IllegalStateException{
	private static final long serialVersionUID = 1L;
	
	public GeneralIllegalStateException(String msg) {
		super(msg);
	}
}