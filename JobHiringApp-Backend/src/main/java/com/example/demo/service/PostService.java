package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Company;
import com.example.demo.entity.Job;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.JobRepository;

@Service
public class PostService {

	@Autowired
	JobRepository jobRepository;
	CompanyRepository companyRepository;
	
	public PostService(JobRepository jobRepository, CompanyRepository companyRepository) {
		this.jobRepository = jobRepository;
		this.companyRepository = companyRepository;
	}
	
	public Job addPost(Job job) {
		
		Job response =  jobRepository.save(job);
		
		if(response == null) {
			throw new IllegalArgumentException("not saved");
		}
		
		return response;
	}

	public Job getJobById(int id) {

		Job job = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Job found"));

		return job;
	}

	public List<Job> getAllPost() {
		List<Job> jobs = jobRepository.findAll();

		return jobs;
	}
	
	public List<Job> searchJobsByKeyword(String keyword) {
        return jobRepository.searchJobsByKeyword(keyword);
    }

    public List<Job> searchJobsByTitleAndLocation(String title, String location) {
        return jobRepository.searchJobsByTitleAndLocation(title, location);
    }

    public List<Job> searchJobsByFilters(String title, String location, double minSalary) {
        return jobRepository.searchJobsByFilters(title, location, minSalary);
    }

    public List<Job> searchJobsByAllFilters(String title, String location, double minSalary) {
        return jobRepository.searchJobsByAllFilters(title, location, minSalary);
    }
    
    
    //get details along with company details(manual fetching)
    public Map<String, Object> getJobWithCompanyDetails(int jobId) {
    	
    	Map<String, Object> response = new HashMap<>();
    	
        Optional<Job> job = jobRepository.findById(jobId);
        
        if (job.isPresent()) {
            response.put("Job", job.get());

            // Fetch company using companyId
            Optional<Company> company = companyRepository.findById(job.get().getCompanyId());
            if(company.isPresent()) {
            	response.put("company", company.get());
            }
        }
        
        return response;
    }


}
