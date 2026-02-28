package com.ok.netflix.clone.netflix_clone.service;

import com.ok.netflix.clone.netflix_clone.dto.request.UserRequest;
import com.ok.netflix.clone.netflix_clone.dto.response.MessageResponse;
import jakarta.validation.Valid;

public interface AuthService {
	MessageResponse signup(@Valid UserRequest userRequest);
}
