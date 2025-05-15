package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Company;
import com.example.demo.entity.Job;
import com.example.demo.repository.JobRepository;
import com.example.demo.service.CompanyService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/companies")
class CompanyController {
	
	CompanyService companyService;
	JobRepository jobRepository;
	
	public CompanyController(CompanyService companyService, JobRepository jobRepository) {
		this.companyService = companyService;
		this.jobRepository = jobRepository;
	}

	@GetMapping("/get")
	public Company getCompanyDetails(@RequestParam String cid) {
		
		int id = Integer.parseInt(cid);
		
		Company company = companyService.getCompanyDetails(id);

		return company;
	}

	@GetMapping("/getall")
	public List<Company> getAllCompanyDetails() {
	
		List<Company> compaines	= companyService.getAllCompanyDetails();
		return compaines;
	}
	
	@GetMapping("/getbyjob")
	public ResponseEntity<?> getCompanyUsingJobId(@RequestParam int id) {
		try {
			Job job =  jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Job not found"));
			Company company = companyService.getCompanyUsingJobId(job);
			
			return ResponseEntity.ok(company);
			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("something went wrong");
		}
	}
}
