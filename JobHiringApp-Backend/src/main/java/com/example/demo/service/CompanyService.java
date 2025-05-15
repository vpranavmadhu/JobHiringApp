package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Company;
import com.example.demo.entity.Job;
import com.example.demo.repository.CompanyRepository;

@Service
public class CompanyService {
	
	@Autowired
	CompanyRepository companyRepository;

	public Company getCompanyDetails(int id) {
		
		 Company company= companyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No companyfound"));
		 
		 return company;
	}
	
	public List<Company> getAllCompanyDetails() {
		List<Company> companies = companyRepository.findAll();
		
		return companies;
		
		
	}
	
	public Company getCompanyUsingJobId(Job job) {
		int id = job.getCompanyId();
		 Company company= companyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No company found"));
		 
		 return company;
		
	}
}
