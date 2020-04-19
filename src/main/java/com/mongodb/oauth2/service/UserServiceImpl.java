package com.mongodb.oauth2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.oauth2.dao.UserDao;
import com.mongodb.oauth2.entity.User;


@Service
public class UserServiceImpl implements UserService{

Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public User findByUsername(String username) {
		log.info("Username:> {}",username);
		return userDao.findByUsername(username);
	}

	@Override
	public User save(User user) {
		log.info("user:> {}",user);
		return userDao.save(user);
	}

}
