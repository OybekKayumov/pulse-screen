package com.ok.netflix.clone.netflix_clone.controller;

import com.ok.netflix.clone.netflix_clone.dto.request.EmailRequest;
import com.ok.netflix.clone.netflix_clone.dto.request.LoginRequest;
import com.ok.netflix.clone.netflix_clone.dto.request.UserRequest;
import com.ok.netflix.clone.netflix_clone.dto.response.EmailValidationResponse;
import com.ok.netflix.clone.netflix_clone.dto.response.LoginResponse;
import com.ok.netflix.clone.netflix_clone.dto.response.MessageResponse;
import com.ok.netflix.clone.netflix_clone.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> signup(@Valid @RequestBody UserRequest userRequest) {

		return ResponseEntity.ok(authService.signup(userRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

		LoginResponse response = authService.login(
						loginRequest.getEmail(), loginRequest.getPassword());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/validate-email")
	public ResponseEntity<EmailValidationResponse> validateEmail(@RequestParam String email) {

		return ResponseEntity.ok(authService.validateEmail(email));
	}

	@GetMapping("/verify-email")
	public ResponseEntity<MessageResponse> verifyEmail(@RequestParam String token) {

		return ResponseEntity.ok(authService.verifyEmail(token));
	}

	@PostMapping("/resend-verification")
	public ResponseEntity<MessageResponse> resendVerification(
					@Valid @RequestBody EmailRequest emailRequest) {

		return ResponseEntity.ok(authService.resendVerification(emailRequest.getEmail()));
	}
}
