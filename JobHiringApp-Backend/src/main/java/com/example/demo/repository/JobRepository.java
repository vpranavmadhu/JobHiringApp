package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.entity.Job;

public interface JobRepository extends MongoRepository<Job, Integer> {

	// Search by job title or description (case-insensitive)
	@Query("{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } }, { 'skills_required': { $regex: ?0, $options: 'i' } } ] }")
	List<Job> searchJobsByKeyword(String keyword);

	// Search by title and location
	@Query("{ 'title': { $regex: ?0, $options: 'i' }, 'location': { $regex: ?1, $options: 'i' } }")
	List<Job> searchJobsByTitleAndLocation(String title, String location);

	// Search by title, location, and minimum salary
	@Query("{ 'title': { $regex: ?0, $options: 'i' }, 'location': { $regex: ?1, $options: 'i' }, 'salary': { $gte: ?2 } }")
	List<Job> searchJobsByFilters(String title, String location, double minSalary);

	// Search by multiple optional filters
	@Query("{ " + "  $and: [ " + "    { 'title': { $regex: ?0, $options: 'i' } }, "
			+ "    { 'location': { $regex: ?1, $options: 'i' } }, " + "    { 'salary': { $gte: ?2 } } " + "  ] " + "}")
	List<Job> searchJobsByAllFilters(String title, String location, double minSalary);

	
	List<Job> findByAdminId(String adminId);

}
