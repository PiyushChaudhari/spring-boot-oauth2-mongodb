package com.mongodb.oauth2.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mongodb.oauth2.entity.Role;



public interface RoleDao extends MongoRepository<Role, String>{

	@Query(value = "{'name' : ?0 }")
	Role findByName(String name);
}
