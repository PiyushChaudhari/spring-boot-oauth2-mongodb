package com.mongodb.oauth2.service;

import com.mongodb.oauth2.entity.User;

public interface UserService {

	public User findByUsername(String username);
	
	public User save(User user);
}
