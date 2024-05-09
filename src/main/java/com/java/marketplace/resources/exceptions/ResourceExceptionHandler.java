package com.java.marketplace.resources.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.java.marketplace.resources.exceptions.utils.StandardMessage;
import com.java.marketplace.services.exceptions.DatabaseException;
import com.java.marketplace.services.exceptions.GeneralIllegalStateException;
import com.java.marketplace.services.exceptions.GeneralPatchException;
import com.java.marketplace.services.exceptions.IllegalFieldException;
import com.java.marketplace.services.exceptions.OrderStatusException;
import com.java.marketplace.services.exceptions.ProductCurrentlyUnavailableForSaleException;
import com.java.marketplace.services.exceptions.ProductOutOfStockException;
import com.java.marketplace.services.exceptions.ResourceNotFoundException;
import com.java.marketplace.services.exceptions.UserAlreadyHasACustomerException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
		String error = "Database error";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(UserAlreadyHasACustomerException.class)
	public ResponseEntity<StandardError> customIllegalState(UserAlreadyHasACustomerException e, HttpServletRequest request) {
		String error = "Illegal association";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(GeneralPatchException.class)
	public ResponseEntity<StandardError> GeneralPatchState(GeneralPatchException e, HttpServletRequest request) {
		String error = "Illegal patch";
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(GeneralIllegalStateException.class)
	public ResponseEntity<StandardError> generalIllegalState(GeneralIllegalStateException e,
			HttpServletRequest request) {
		String error = "Illegal association";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(IllegalFieldException.class)
	public ResponseEntity<StandardError> illegalField(IllegalFieldException e, HttpServletRequest request) {
		String error = "Illegal field value";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardError> httpMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
		String error = "Request body not readable";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(OrderStatusException.class)
	public ResponseEntity<StandardError> orderStatus(OrderStatusException e, HttpServletRequest request) {
		String error = "Status unavailable for the order";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(ProductOutOfStockException.class)
	public ResponseEntity<StandardError> productOutOfStock(ProductOutOfStockException e, HttpServletRequest request) {
		String error = "Unvaliable product";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(ProductCurrentlyUnavailableForSaleException.class)
	public ResponseEntity<StandardError> productCurrentlyUnavailableForSale(
			ProductCurrentlyUnavailableForSaleException e, HttpServletRequest request) {
		String error = "Unvaliable product";
		HttpStatus status = HttpStatus.FORBIDDEN;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
		Map<String, String> errs = StandardMessage.errsMessage(e);
		String error = "Invalid argument value";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, errs, request.getRequestURI());
		return ResponseEntity.status(status).body(err);		
	}
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<StandardError> integrityConstraintViolation(SQLIntegrityConstraintViolationException e, HttpServletRequest request) {
		String error = "Integrity constraint violation";
		HttpStatus status = HttpStatus.CONFLICT;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, StandardMessage.getExceptionMessagePerField(e.getMessage()),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<StandardError> httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
		String error = "Not implemented";
		String msg = "The requested endpoint or HTTP method is not implemented";
		HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
		StandardError err = new StandardError(Instant.now(), status.value(), error, msg, request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<StandardError> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
		String error = "Not found";
		String msg = "The requested endpoint does not exist";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, msg, request.getRequestURI());
		return ResponseEntity.status(status).body(err);
    }
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<StandardError> noSuchElement(NoSuchElementException e, HttpServletRequest request) {
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
}
