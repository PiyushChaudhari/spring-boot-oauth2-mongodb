package com.mongodb.oauth2.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.oauth2.dao.PermissionDao;
import com.mongodb.oauth2.entity.Permission;


@Transactional
@Service
public class PermissionServiceImpl implements PermissionService {

	Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Override
	public Permission findByName(String name) {
		log.info("findByName name:> {}",name);
		return permissionDao.findByName(name);
	}

	@Override
	public Permission get(String id) {
		log.info("get id:> {}",id);
		Optional<Permission> permissionDetails = permissionDao.findById(id);  
		return permissionDetails.isPresent() ? permissionDetails.get() : null;
	}

}
