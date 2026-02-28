package com.ok.netflix.clone.netflix_clone.service;

public interface EmailService {

	void senVerificationEmail(String toEmail, String token);

	void sendPasswordResetEmail(String toEmail, String token);

}
