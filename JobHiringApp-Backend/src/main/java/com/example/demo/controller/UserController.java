package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.Userappliedjobs;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserappliedjobsReopsitory;
import com.example.demo.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/user")
public class UserController {

	UserRepository userRepository;
	UserService userService;
	UserappliedjobsReopsitory userappliedjobsReopsitory;

	public UserController(UserRepository userRepository, UserService userService,
			UserappliedjobsReopsitory userappliedjobsReopsitory) {
		this.userRepository = userRepository;
		this.userService = userService;
		this.userappliedjobsReopsitory = userappliedjobsReopsitory;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			User registeredUser = userService.register(user);
			return ResponseEntity.ok(Map.of("message", "registration successful", "user", registeredUser));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> authentication(@RequestBody LoginRequest request, HttpServletResponse response) {

		try {

			User user = userService.authentication(request.getUsername(), request.getPassword());
			String token = userService.generateToken(user);
			Cookie cookie = new Cookie("authToken", token);
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
			cookie.setPath("/");
			cookie.setMaxAge(14400);
			response.addCookie(cookie);

			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", "login successfull");
			responseBody.put("username", user.getUsername());
			responseBody.put("role", user.getRole().name());

			return ResponseEntity.ok(responseBody);

		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error", e.getMessage()));
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			userService.logout(user);
			Cookie cookie = new Cookie("authenticatedUser", null);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);

			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", "Logout successful");
			System.out.println("logout performed");
			return ResponseEntity.ok(responseBody);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("Error", "internal server error"));
		}
	}

	@GetMapping("/getUser")
	public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("Error", "internal server error"));
		}
	}

	@PostMapping("/apply")
	public ResponseEntity<?> userapply(@RequestParam int id, HttpServletRequest request) {
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			String userId = user.getId();
			int jobId = id;

			Userappliedjobs userappliedjobs = new Userappliedjobs(userId, jobId);

			if (userappliedjobsReopsitory.findByJobId(jobId)!= null) {
				if (userappliedjobsReopsitory.findByJobId(jobId).contains(userId)) {
					return ResponseEntity.ok("You Have already applied for the job");
				}
			}

			userappliedjobsReopsitory.save(userappliedjobs);
			return ResponseEntity.ok("Job Application Successfull....!");

		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("something went wrong");
		}
	}
	
	@DeleteMapping("/deleteuser")
	public ResponseEntity<?> deleteUser(HttpServletRequest request) {
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			userRepository.delete(user);
			return ResponseEntity.ok("Deleted Succesfully...!");
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("something went wrong");
		}
	}
}
