package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "JWTTokens")
public class JWTToken {
	
	@Id
	String id;
	String token;
	@Field(name = "user_id")
	String userId;
	String username;
	@Field(name = "created_at")
	LocalDateTime createdAt;
	@Field(name = "expires_at")
	LocalDateTime expiresAt;
	
	public JWTToken() {
		// TODO Auto-generated constructor stub
	}

	

	public JWTToken(String token, String userId, String username, LocalDateTime createdAt, LocalDateTime expiresAt) {
		super();
		this.token = token;
		this.userId = userId;
		this.username = username;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}



	public JWTToken(String id, String token, String userId, String username, LocalDateTime createdAt,
			LocalDateTime expiresAt) {
		super();
		this.id = id;
		this.token = token;
		this.userId = userId;
		this.username = username;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	
	
	
	
	

}
