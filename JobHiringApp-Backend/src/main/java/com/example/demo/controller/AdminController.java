package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.entity.Userappliedjobs;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserappliedjobsReopsitory;
import com.example.demo.service.PostService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminController {

	JobRepository jobRepository;
	PostService postService;
	UserappliedjobsReopsitory userappliedjobsReopsitory;
	UserRepository userRepository;

	public AdminController(JobRepository jobRepository, PostService postService,
			UserappliedjobsReopsitory userappliedjobsReopsitory, UserRepository userRepository) {
		this.jobRepository = jobRepository;
		this.postService = postService;
		this.userappliedjobsReopsitory = userappliedjobsReopsitory;
		this.userRepository = userRepository;
	}

	// get admin details
	@GetMapping("/get")
	public ResponseEntity<?> getAdminDetails(HttpServletRequest request) {
		try {
			User admin = (User) request.getAttribute("authenticatedUser");

			Map<String, Object> response = new HashMap<>();

			response.put("username", admin.getUsername());
			response.put("email", admin.getEmail());

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	// add new post by admin
	@PostMapping("/add")
	public ResponseEntity<?> addPost(@RequestBody Map<String, Object> request, HttpServletRequest req) {
		System.out.println(request);
		User admin = (User) req.getAttribute("authenticatedUser");
		String adminId = admin.getId();

		try {
			int companyId = Integer.parseInt((String) request.get("companyId"));
			String title = (String) request.get("title");
			String description = (String) request.get("description");
			String location = (String) request.get("location");
			String jobType = (String) request.get("jobType");
			String skills = (String) request.get("skills");
			List<String> skillsRequired = Arrays.asList(skills.split(","));
			double salary = Double.valueOf((String) request.get("salary"));
			String experienceRequired = (String) request.get("exp");
			Random random = new Random();
			int id = random.nextInt(999);
			Job job = new Job(id, title, description, skillsRequired, companyId, salary, location, "Open",
					LocalDateTime.now(), jobType, experienceRequired, adminId);
			;
			Job response = postService.addPost(job);

			return ResponseEntity.ok(response);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("something went wrong");
		}

	}

	// veiw jobs posted by admin
	@GetMapping("/getjobs")
	public ResponseEntity<?> veiwJobPostedByAdmin(HttpServletRequest request) {

		try {
			User admin = (User) request.getAttribute("authenticatedUser");

			String adminId = admin.getId();

			List<Job> jobs = jobRepository.findByAdminId(adminId);

			return ResponseEntity.ok(jobs);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}

	}

	@DeleteMapping("/deletejob")
	public ResponseEntity<?> deleteJobByAdmin(@RequestParam int jobId) {
		try {

			jobRepository.deleteById(jobId);

			return ResponseEntity.ok("Deleted Successfully...!");

		} catch (EmptyResultDataAccessException e) {

			return ResponseEntity.status(404).body("Job not found");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	@PutMapping("/updatestatus")
	public ResponseEntity<?> updateStatusByAdmin(@RequestBody Map<String, Object> request) {
		try {

			int jobId = (int) request.get("jobId");
			String status = (String) request.get("status");

			Optional<Job> job = jobRepository.findById(jobId);

			if (job.isPresent()) {
				Job existingJob = job.get();
				existingJob.setStatus(status);
				jobRepository.save(existingJob);
				return ResponseEntity.ok("Status Updated!");
			} else {
				throw new EmptyResultDataAccessException("Job not found", 1);
			}

		} catch (EmptyResultDataAccessException e) {

			return ResponseEntity.status(404).body("Job not found");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	@GetMapping("/viewapplicants")
	public ResponseEntity<?> veiwApplicants(@RequestParam int jobId) {

		try {
			
			List<Userappliedjobs> appliedUsers = userappliedjobsReopsitory.findByJobId(jobId);
			
			List<String> userIds = appliedUsers.stream()
                    .map(Userappliedjobs::getUserId)
                    .collect(Collectors.toList());
			
			List<User> users = userRepository.findByIdIn(userIds);
			
			if (users.isEmpty()) {
				return ResponseEntity.status(404).body("No applicants found for the job ID: " + jobId);
			}
			
			return ResponseEntity.ok(users);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

}
