package com.example.JavaCafe;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "resettoken")
public class ResetPasswordToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String emailToken;
	private LocalDateTime createdAt;
	private LocalDateTime expiresAt;
	
	@ManyToOne
	private User user;
	
	public ResetPasswordToken(String emailToken, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
		super();
		this.emailToken = emailToken;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
		this.user = user;
	}
	public ResetPasswordToken() {
    }
	public Long getId() {
		return id;
	}
	public String getEmailToken() {
		return emailToken;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
