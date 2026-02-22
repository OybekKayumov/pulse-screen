package com.ok.netflix.clone.netflix_clone.dao;

import com.ok.netflix.clone.netflix_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
