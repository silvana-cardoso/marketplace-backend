package com.java.marketplace.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(Class<?> entityClass, Object id) {
		super(entityClass.getSimpleName() + " not found with id: " + id);
	}
	
}
