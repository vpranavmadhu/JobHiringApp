package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	List<User> findByIdIn(List<String> ids);

}
