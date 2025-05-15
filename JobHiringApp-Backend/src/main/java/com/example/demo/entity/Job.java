package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Jobs")
public class Job {
	
	@Id
	int id;
	String title;
	String description;
	@Field(name = "skills_required")
	List<String> skillsRequired;
	@Field(name = "company_id")
	int companyId;
	double salary;
	String location;
	String status;
	@Field(name = "posted_on")
	LocalDateTime postedOn;
	@Field(name = "job_type")
	String jobType;
	@Field(name = "experience_required")
	String experienceRequired;
	@Field(name = "admin_id")
	private String adminId;

	public Job() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Job(String title, String description, List<String> skillsRequired, int companyId, double salary,
			String location, String status, LocalDateTime postedOn, String jobType, String experienceRequired, String adminId) {
		super();
		this.title = title;
		this.description = description;
		this.skillsRequired = skillsRequired;
		this.companyId = companyId;
		this.salary = salary;
		this.location = location;
		this.status = status;
		this.postedOn = postedOn;
		this.jobType = jobType;
		this.experienceRequired = experienceRequired;
		this.adminId = adminId;
	}



	public Job(int id, String title, String description, List<String> skillsRequired, int companyId, double salary,
			String location, String status, LocalDateTime postedOn, String jobType, String experienceRequired, String adminId) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.skillsRequired = skillsRequired;
		this.companyId = companyId;
		this.salary = salary;
		this.location = location;
		this.status = status;
		this.postedOn = postedOn;
		this.jobType = jobType;
		this.experienceRequired = experienceRequired;
		this.adminId = adminId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getSkillsRequired() {
		return skillsRequired;
	}

	public void setSkillsRequired(List<String> skillsRequired) {
		this.skillsRequired = skillsRequired;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(LocalDateTime postedOn) {
		this.postedOn = postedOn;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getExperienceRequired() {
		return experienceRequired;
	}

	public void setExperienceRequired(String experienceRequired) {
		this.experienceRequired = experienceRequired;
	}



	public String getAdminId() {
		return adminId;
	}



	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	
	
	
}
