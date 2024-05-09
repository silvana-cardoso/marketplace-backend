package com.java.marketplace.resources.exceptions.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class StandardMessage {

	public static Map<String, String> errsMessage(MethodArgumentNotValidException e) {
		Map<String, String> errs = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((err) -> {
			String fieldName = ((FieldError) err).getField();
			String errorMessage = err.getDefaultMessage();
			errs.put(fieldName, errorMessage);
		});
		
		return errs;
	}
	
	public static String getExceptionMessagePerField(String default_msg) {
		String msg;
		if (default_msg.contains("EMAIL NULLS FIRST")) {
			msg = "Email address has already been registered";
		} else if (default_msg.contains("DOCUMENT_NUMBER NULLS FIRST")) {
			msg = "Document number has already been registered";
		} else if (default_msg.contains("ADDRESS NULLS FIRST")) {
			msg = "Address has already been registered for this customer";
		}
		else {
			return default_msg;
		}
		
		return msg;
	}
}
