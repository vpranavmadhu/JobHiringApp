package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;
import com.example.demo.entity.Userappliedjobs;
import java.util.List;


@Repository
public interface UserappliedjobsReopsitory extends MongoRepository<Userappliedjobs, String>{

//	Optional<Userappliedjobs> findByJobId(int jobId);
	
	 List<Userappliedjobs> findByJobId(int jobId);
	
}
