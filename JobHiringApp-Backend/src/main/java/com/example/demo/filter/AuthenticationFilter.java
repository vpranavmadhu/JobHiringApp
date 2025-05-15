package com.example.demo.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "/api/*", "/admin/" })
@Component
public class AuthenticationFilter implements Filter {

	private final UserService userService;
	private final UserRepository userRepository;
	private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	private static final String ALLOWED_ORIGIN = "http://localhost:5173";
	private static final String[] UNAUTHENTICATED_PATHS = {
		"/api/user/register",
		"/api/user/login"
	};

	public AuthenticationFilter(UserService userService, UserRepository userRepository) {
		this.userRepository = userRepository;
		this.userService = userService;
		System.out.println("Filter initialized");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			executeFilterLogic(request, response, chain);
		} catch (Exception e) {
			logger.error("Unexpected error in AuthenticationFilter", e);
			sendErrorResponse((HttpServletResponse) response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Internal server error");
		}
	}

	private void executeFilterLogic(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requestURI = httpRequest.getRequestURI();

		// Always set CORS headers
		setCORSHeaders(httpResponse);

		// Allow preflight requests
		if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
			return;
		}

		// Allow unauthenticated paths
		if (Arrays.stream(UNAUTHENTICATED_PATHS).anyMatch(requestURI::startsWith)) {
			chain.doFilter(request, response);
			return;
		}

		// Extract and validate token
		String token = getAuthTokenFromCookies(httpRequest);
		if (token == null || !userService.validateToken(token)) {
			logger.warn("Invalid or missing token: {}", token);
			sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
			return;
		}

		// Extract user ID and validate user existence
		String userId = userService.extractUserId(token);
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isEmpty()) {
			sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
			return;
		}

		User authenticatedUser = userOptional.get();
		Role role = authenticatedUser.getRole();
		logger.info("Authenticated User: {}, Role: {}", authenticatedUser.getUsername(), role);
		logger.info("Filter triggered for: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());


		// Allow logout for all roles
		if (requestURI.contains("/api/user/logout")) {
			httpRequest.setAttribute("authenticatedUser", authenticatedUser);
			chain.doFilter(request, response);
			return;
		}

		// Role-based access control
		if (requestURI.startsWith("/admin/") && role != Role.ADMIN) {
			sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Forbidden: Admin access required");
			return;
		}

		if (requestURI.startsWith("/api/") && role != Role.SEEKER) {
			sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Forbidden: JobSeeker access required");
			return;
		}

		// Attach authenticated user to request
		httpRequest.setAttribute("authenticatedUser", authenticatedUser);
		chain.doFilter(request, response);
	}

	private void setCORSHeaders(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
	}

	private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
		response.setStatus(statusCode);
		response.getWriter().write(message);
	}

	private String getAuthTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			return Arrays.stream(cookies)
					.filter(cookie -> "authToken".equals(cookie.getName()))
					.map(Cookie::getValue)
					.findFirst()
					.orElse(null);
		}
		return null;
	}
}
