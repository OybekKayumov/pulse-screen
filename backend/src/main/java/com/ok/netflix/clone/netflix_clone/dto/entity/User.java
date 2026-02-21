package com.ok.netflix.clone.netflix_clone.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ok.netflix.clone.netflix_clone.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role = Role.USER;

	@Column(nullable = false)
	private boolean active = true;

	@Column(nullable = false)
	private boolean emailVerified = false;

	@Column(unique = true)
	private String verificationToken;

	@Column
	private Integer verificationTokenExpiry;

	@Column
	private String passwordResetToken;

	@Column
	private String passwordResetTokenExpiry;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private Instant updatedAt;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
					name = "user_watchlist",
					joinColumns = @JoinColumn(name = "user_id"),
					inverseJoinColumns = @JoinColumn(name = "video_id")
	)
	private Set<Video> watchList = new HashSet<>();

	public void addToWatchList(Video video) {
		this.watchList.add(video);
	}

	public void removeFromWatchList(Video video) {
		this.watchList.remove(video);
	}
}
