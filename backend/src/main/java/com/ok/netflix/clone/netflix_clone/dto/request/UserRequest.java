package com.ok.netflix.clone.netflix_clone.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {

	private String email;

	private String password;

	private String fullName;

	private String role;

	private Boolean active;
}
