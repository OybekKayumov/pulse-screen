package com.ok.netflix.clone.netflix_clone.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log =  LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BadCredentialException.class)
	public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialException ex) {

		log.warn("BadCredentialsException: {}", ex.getMessage(), ex);
		return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	@ExceptionHandler(AccountDeactivatedException.class)
	public ResponseEntity<Map<String, Object>> handleAccountDeactivated(AccountDeactivatedException ex) {

		log.warn("AccountDeactivatedException: {}", ex.getMessage(), ex);
		return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
	}

	private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {

		Map<String, Object> body =Map.of("timestamp", Instant.now(), "error", message);
		return ResponseEntity.status(status).body(body);
	}
}
