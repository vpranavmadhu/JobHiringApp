package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Userappliedjobs")
public class Userappliedjobs {
	
	@Id
	String id;
	@Field(name = "user_id")
	String userId;
	@Field(name = "job_id")
	int jobId;
	
	public Userappliedjobs() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Userappliedjobs(String userId, int jobId) {
		super();
		this.userId = userId;
		this.jobId = jobId;
	}



	public Userappliedjobs(String id, String userId, int jobId) {
		super();
		this.id = id;
		this.userId = userId;
		this.jobId = jobId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
	
	

}
