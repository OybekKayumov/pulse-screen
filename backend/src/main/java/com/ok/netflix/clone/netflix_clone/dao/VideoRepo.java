package com.ok.netflix.clone.netflix_clone.dao;

import com.ok.netflix.clone.netflix_clone.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepo extends JpaRepository<Video, Long> {
}
