package com.ok.netflix.clone.netflix_clone.util;

import com.ok.netflix.clone.netflix_clone.dao.UserRepo;
import com.ok.netflix.clone.netflix_clone.dao.VideoRepo;
import com.ok.netflix.clone.netflix_clone.entity.User;
import com.ok.netflix.clone.netflix_clone.entity.Video;
import com.ok.netflix.clone.netflix_clone.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtils {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private VideoRepo videoRepo;

	public User getUserByEmailOrThrow(String email) {

		return userRepo.findByEmail(email)
						.orElseThrow(() -> new ResourceNotFoundException(
										"User with email " + email + " not found"));
	}

	public User getUserByIdOrThrow(Long id) {

		return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
						"User with id " + id + " not found"));
	}

	public Video getVideoByIdOrThrow(Long id) {
		return videoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
						"Video with id " + id + " not found"));
	}
}
