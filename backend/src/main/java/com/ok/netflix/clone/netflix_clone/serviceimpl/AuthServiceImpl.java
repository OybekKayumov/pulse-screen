package com.ok.netflix.clone.netflix_clone.serviceimpl;

import com.ok.netflix.clone.netflix_clone.dao.UserRepo;
import com.ok.netflix.clone.netflix_clone.dto.request.UserRequest;
import com.ok.netflix.clone.netflix_clone.dto.response.EmailValidationResponse;
import com.ok.netflix.clone.netflix_clone.dto.response.LoginResponse;
import com.ok.netflix.clone.netflix_clone.dto.response.MessageResponse;
import com.ok.netflix.clone.netflix_clone.entity.User;
import com.ok.netflix.clone.netflix_clone.enums.Role;
import com.ok.netflix.clone.netflix_clone.exception.*;
import com.ok.netflix.clone.netflix_clone.security.JwtUtil;
import com.ok.netflix.clone.netflix_clone.service.AuthService;
import com.ok.netflix.clone.netflix_clone.service.EmailService;
import com.ok.netflix.clone.netflix_clone.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@Override
	public LoginResponse login(String email, String password) {
		User user = userRepo.findByEmail(email)
						.filter(u -> passwordEncoder.matches(password, u.getPassword()))
						.orElseThrow(() -> new BadCredentialException(
										"Invalid email or password"));

		if (!user.isActive()) {
			throw new AccountDeactivatedException("Account has been deactivated");
		}

		if (!user.isEmailVerified()) {
			throw new EmailNotVerifiedException("Please verify your email before logging in");
		}

		final String token = jwtUtil.generateToken(
						user.getEmail(), user.getRole().name());

		return new LoginResponse(
						token,
						user.getEmail(),
						user.getFullName(),
						user.getRole().name());
	}

	@Override
	public EmailValidationResponse validateEmail(String email) {

		boolean exists = userRepo.existsByEmail(email);

		return new EmailValidationResponse(exists, !exists);
	}

	@Override
	public MessageResponse verifyEmail(String token) {

		User user = userRepo.findByVerificationToken(token)
						.orElseThrow(() -> new InvalidTokenException(
										"Invalid or expired verification token"));


		if (user.getVerificationTokenExpiry() == null
			|| user.getVerificationTokenExpiry().isBefore(Instant.now())) {
			throw new InvalidTokenException("Invalid or expired verification token");
		}

		user.setEmailVerified(true);
		user.setVerificationToken(null);
		user.setVerificationTokenExpiry(null);
		userRepo.save(user);

		return  new MessageResponse("Verification successful")  ;
	}

	@Override
	public MessageResponse resendVerification(String email) {

		User user = serviceUtils.getUserByEmailOrThrow(email);

		String verificationToken = UUID.randomUUID().toString();
		user.setVerificationToken(verificationToken);
		user.setVerificationTokenExpiry(Instant.now().plusSeconds(864000));
		userRepo.save(user);
		emailService.senVerificationEmail(email, verificationToken);

		return new MessageResponse("Verification successful")  ;
	}

	@Override
	public MessageResponse forgotPassword(String email) {

		User user = serviceUtils.getUserByEmailOrThrow(email);
		String resetToken = UUID.randomUUID().toString();
		user.setPasswordResetToken(resetToken);
		user.setPasswordResetTokenExpiry(Instant.now().plusSeconds(3600));

		userRepo.save(user);
		emailService.sendPasswordResetEmail(email, resetToken);

		return new MessageResponse("Password reset email sent successful");
	}

	@Override
	public MessageResponse resetPassword(String token, String newPassword) {

		User user = userRepo.findByPasswordResetToken(token)
						.orElseThrow(() -> new InvalidTokenException("Invalid token"));

		if (user.getPasswordResetTokenExpiry() == null
				|| user.getPasswordResetTokenExpiry().isBefore(Instant.now())) {

			throw new InvalidTokenException("Reset token expired");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		user.setPasswordResetToken(null);
		user.setPasswordResetTokenExpiry(null);
		userRepo.save(user);

		return new MessageResponse("Password reset successful");
	}

	@Override
	public MessageResponse changePassword(String email, String currentPassword, String newPassword) {

		User user = serviceUtils.getUserByEmailOrThrow(email);

		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			throw new InvalidCredentialsException("Invalid password");
		}

		user.setPassword(passwordEncoder.encode(newPassword));

		userRepo.save(user);

		return new MessageResponse("Password changed successful");
	}

}
