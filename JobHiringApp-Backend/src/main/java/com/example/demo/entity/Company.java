package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Companies")
public class Company {

	@Id
	int id;
	String name;
	String location;
	String industry;
	String website;
	int foundedYear;
	int employees;
	
	public Company() {
		// TODO Auto-generated constructor stub
	}

	public Company(int id, String name, String location, String industry, String website, int foundedYear,
			int employees) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.industry = industry;
		this.website = website;
		this.foundedYear = foundedYear;
		this.employees = employees;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public int getFoundedYear() {
		return foundedYear;
	}

	public void setFoundedYear(int foundedYear) {
		this.foundedYear = foundedYear;
	}

	public int getEmployees() {
		return employees;
	}

	public void setEmployees(int employees) {
		this.employees = employees;
	}
	
	
}
