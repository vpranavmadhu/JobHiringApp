package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.JWTToken;
import java.util.Optional;


public interface JWTTokenRepository extends MongoRepository<JWTToken, String>{
	
	JWTToken findByUserId(String userId);
	
	Optional<JWTToken> findByToken(String token);
	
	@Transactional
	void deleteByUserId(String userId);
	
	

}
