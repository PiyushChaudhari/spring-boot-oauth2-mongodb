package com.mongodb.oauth2.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mongodb.oauth2.entity.User;


public interface UserDao extends MongoRepository<User, String>{
	
	@Query(value = "{'username' : ?0 }")
	User findByUsername(String username);

}
