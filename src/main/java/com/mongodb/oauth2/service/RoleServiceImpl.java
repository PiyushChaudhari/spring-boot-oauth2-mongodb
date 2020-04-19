package com.mongodb.oauth2.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.oauth2.dao.RoleDao;
import com.mongodb.oauth2.entity.Role;


@Transactional
@Service
public class RoleServiceImpl implements RoleService {

	Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public Role findByName(String name) {
		log.info("findByName name:> {}",name);
		return roleDao.findByName(name);
	}

	@Override
	public Role get(String id) {
		log.info("get id:> {}",id);
		Optional<Role> roleDetails = roleDao.findById(id);  
		return roleDetails.isPresent() ? roleDetails.get() : null;
	}

}
