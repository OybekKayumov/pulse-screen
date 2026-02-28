package com.ok.netflix.clone.netflix_clone.dao;

import com.ok.netflix.clone.netflix_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}
