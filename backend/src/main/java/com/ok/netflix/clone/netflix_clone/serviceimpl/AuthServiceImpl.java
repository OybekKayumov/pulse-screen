package com.ok.netflix.clone.netflix_clone.serviceimpl;

import com.ok.netflix.clone.netflix_clone.dao.UserRepo;
import com.ok.netflix.clone.netflix_clone.dto.request.UserRequest;
import com.ok.netflix.clone.netflix_clone.dto.response.MessageResponse;
import com.ok.netflix.clone.netflix_clone.entity.User;
import com.ok.netflix.clone.netflix_clone.enums.Role;
import com.ok.netflix.clone.netflix_clone.exception.EmailAlreadyExistsException;
import com.ok.netflix.clone.netflix_clone.security.JwtUtil;
import com.ok.netflix.clone.netflix_clone.service.AuthService;
import com.ok.netflix.clone.netflix_clone.service.EmailService;
import com.ok.netflix.clone.netflix_clone.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ServiceUtils serviceUtils;


	@Override
	public MessageResponse signup(UserRequest userRequest) {

		if (userRepo.existsByEmail(userRequest.getEmail())) {

			throw new EmailAlreadyExistsException("Email already exists");
		}

		User user = new User();
		user.setEmail(userRequest.getEmail());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setFullName(userRequest.getFullName());
		user.setRole(Role.USER);
		user.setActive(true);
		user.setEmailVerified(false);
		String verificationToken = UUID.randomUUID().toString();
		user.setVerificationToken(verificationToken);
		user.setVerificationTokenExpiry(Instant.now().plusSeconds(864000));
		userRepo.save(user);
		emailService.senVerificationEmail(userRequest.getEmail(), verificationToken);

		return new MessageResponse(
						"Registration successful! Please check your email to verify your " +
										"account");
	}
}
