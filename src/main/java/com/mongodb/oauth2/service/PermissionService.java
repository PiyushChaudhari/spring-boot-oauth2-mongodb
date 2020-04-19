package com.mongodb.oauth2.service;

import com.mongodb.oauth2.entity.Permission;

public interface PermissionService {

	Permission findByName(String name);
	
	Permission get(String id);
}
