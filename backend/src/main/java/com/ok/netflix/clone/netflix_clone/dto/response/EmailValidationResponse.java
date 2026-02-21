package com.ok.netflix.clone.netflix_clone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EmailValidationResponse {

	private boolean exists;
	private boolean available;

}
