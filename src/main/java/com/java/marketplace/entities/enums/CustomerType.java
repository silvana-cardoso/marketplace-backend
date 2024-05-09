package com.java.marketplace.entities.enums;

public enum CustomerType {
	
	LegalPerson(1),
	NaturalPerson(2),
	Technical(3);
	
	private int code;
	
	private CustomerType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static CustomerType valueOf(int code) {
		for (CustomerType value : CustomerType.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid CustomerType code");
	}
}
