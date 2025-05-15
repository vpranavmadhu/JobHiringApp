package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.repository.JobRepository;
import com.example.demo.service.PostService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/post")
public class PostController {

	@Autowired
	PostService postService;

	@Autowired
	JobRepository jobRepository;

	@GetMapping("/get")
	public ResponseEntity<?> getJobById(@RequestParam int id) {
		Job job = postService.getJobById(id);

		return ResponseEntity.ok(job);
	}

	@GetMapping("/getall")
	public ResponseEntity<?> getAllPost() {

		List<Job> jobs = postService.getAllPost();

		return ResponseEntity.ok(jobs);
	}

	@PostMapping("/add")
	public ResponseEntity<?> addPost(@RequestBody Map<String, Object> request, HttpServletRequest req) {
		System.err.println("reachedcontroller");
		System.out.println(request);
		User admin = (User)req.getAttribute("authenticatedUser");
		String adminId = admin.getId();
		
		try {
		System.err.println("inside try");
		int companyId = Integer.parseInt((String)request.get("companyId"));
		System.err.println(companyId);
		String title = (String) request.get("title");
		System.err.println(title);
		String description = (String) request.get("description");
		System.err.println(description);
		String location = (String) request.get("location");
		String jobType = (String) request.get("jobType");
		System.out.println(jobType);
		String skills = (String) request.get("skills");
		System.err.println(skills);
		List<String> skillsRequired = Arrays.asList(skills.split(","));
		System.err.println(skillsRequired);
		double salary = Double.valueOf((String)request.get("salary"));
		String experienceRequired = (String) request.get("exp");
		System.err.println("creating job");
		
		Random random = new Random();
		int id = random.nextInt(999);
		
		Job job = new Job(id,title, description, skillsRequired, companyId, salary, location, "Open", LocalDateTime.now(), jobType, experienceRequired,adminId);
		System.out.println(job);
		System.err.println("going inside service");
		Job response = postService.addPost(job);
		
		return ResponseEntity.ok(response);
		
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("something went wrong");
		}
		
		
		
	}
	
	@GetMapping("/search")
    public List<Job> searchJobs(@RequestParam(required = false) String title,
                                @RequestParam(required = false) String location,
                                @RequestParam(required = false) Double minSalary) {
        if (title != null && location != null && minSalary != null) {
            return postService.searchJobsByAllFilters(title, location, minSalary);
        } else if (title != null && location != null) {
            return postService.searchJobsByTitleAndLocation(title, location);
        } else if (title != null) {
            return postService.searchJobsByKeyword(title);
        } else {
            return postService.searchJobsByFilters("", "", 0.0); // Return all jobs if no filters are provided
        }
    }

	@GetMapping("getwithcom")
	public ResponseEntity<?> getJobWithCompanyDetails(@RequestParam int id) {
		try {
			Map<String, Object> response = new HashMap<>();
			
			response = postService.getJobWithCompanyDetails(id);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("something went wrong");
		}
	}
	
	public ResponseEntity<?> deleteJobPost(@RequestParam int id) {
		try {
			jobRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}catch (Exception e) {
			return ResponseEntity.internalServerError().body("something went wrong");
		}
	}
	

}
