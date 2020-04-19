package com.mongodb.oauth2.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mongodb.oauth2.entity.Permission;



public interface PermissionDao extends MongoRepository<Permission, String>{

	@Query(value = "{'name' : ?0 }")
	Permission findByName(String name);
}
