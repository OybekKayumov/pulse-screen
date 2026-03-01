package com.ok.netflix.clone.netflix_clone.service;

import com.ok.netflix.clone.netflix_clone.dto.request.UserRequest;
import com.ok.netflix.clone.netflix_clone.dto.response.EmailValidationResponse;
import com.ok.netflix.clone.netflix_clone.dto.response.LoginResponse;
import com.ok.netflix.clone.netflix_clone.dto.response.MessageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface AuthService {
	MessageResponse signup(@Valid UserRequest userRequest);

	LoginResponse login(String email, String password);

	EmailValidationResponse validateEmail(String email);
}
