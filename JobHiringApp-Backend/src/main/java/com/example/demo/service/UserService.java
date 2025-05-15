package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.JWTToken;
import com.example.demo.entity.User;
import com.example.demo.repository.JWTTokenRepository;
import com.example.demo.repository.UserRepository;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class UserService {

	final private Key SIGNING_KEY;
	final UserRepository userRepository;
	final JWTTokenRepository jwtTokenRepository;
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserService(UserRepository userRepository, JWTTokenRepository jwtTokenRepository, @Value("${jwt.secret}") String jwtSecret) {
		this.userRepository = userRepository;
		this.jwtTokenRepository = jwtTokenRepository;
		this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	// register
	public User register(User user) {
		if (userRepository.findByUsername(user.getUsername().strip()).isPresent()) {
			throw new RuntimeException("Username already taken");
		}
		if (userRepository.findByEmail(user.getEmail().strip()).isPresent()) {
			throw new RuntimeException("Email ID already taken");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword().strip()));
		return userRepository.save(user);
	}

	// login
	public User authentication(String username, String password) {
		Optional<User> existingUser = userRepository.findByUsername(username.strip());
		if (existingUser.isPresent()) {
			User user = existingUser.get();
			if (passwordEncoder.matches(password.strip(), user.getPassword())) {
				return user;
				
			} else {
				throw new RuntimeException("Invalid Password");
			}
		} else {
			throw new RuntimeException("Invalid Username");
		}

	}

	public String generateToken(User user) {
		String token;
		LocalDateTime currentTime = LocalDateTime.now();
		JWTToken extoken = jwtTokenRepository.findByUserId(user.getId());
		if (extoken != null && currentTime.isBefore(extoken.getExpiresAt())) {
			token = extoken.getToken();
		} else {
			token = generateNewToken(user);
			if (extoken != null) {
				jwtTokenRepository.delete(extoken);
			}
			saveToken(user, token);
		}
		return token;
	}

	// generating new token
	public String generateNewToken(User user) {
		JwtBuilder builder = Jwts.builder();
		builder.setSubject(user.getId());
		builder.claim("role", user.getRole().name());
		builder.setIssuedAt(new Date());
		builder.setExpiration(new Date(System.currentTimeMillis() + 3600000));
		builder.signWith(SIGNING_KEY);
		String token = builder.compact();
		return token;
	}

	public void saveToken(User user, String token) {
		JWTToken jwtToken = new JWTToken(token, user.getId(),user.getUsername() , LocalDateTime.now(), LocalDateTime.now().plusHours(1));
		jwtTokenRepository.save(jwtToken);
	}

	public boolean validateToken(String token) {
		System.out.println("validating token");
		try {
			// parse and validate token
			Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token);

			// check if token is present in DB and is not expired
			Optional<JWTToken> jwtToken = jwtTokenRepository.findByToken(token);
			if (jwtToken.isPresent()) {
				return jwtToken.get().getExpiresAt().isAfter(LocalDateTime.now());
			}

			return false;

		} catch (Exception e) {
			System.out.println("Token validation Failed " + e.getMessage());
			return false;

		}
	}

	public String extractUserId(String token) {
		return Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token).getBody().getSubject();

	}

	public void logout(User user) {
		jwtTokenRepository.deleteByUserId(user.getId());
	}
}
