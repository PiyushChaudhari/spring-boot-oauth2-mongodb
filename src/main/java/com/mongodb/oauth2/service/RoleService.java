package com.mongodb.oauth2.service;

import com.mongodb.oauth2.entity.Role;

public interface RoleService {

	Role findByName(String name);
	
	Role  get(String id);
}
