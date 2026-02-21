package com.ok.netflix.clone.netflix_clone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

	private String token;

	private String email;

	private String fullname;

	private String role;
}
