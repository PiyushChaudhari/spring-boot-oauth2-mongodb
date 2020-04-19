package com.mongodb.oauth2.init;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class BootStrap implements ApplicationListener<ContextRefreshedEvent>{

	Logger logger = LoggerFactory.getLogger(BootStrap.class);
	
	@Resource
	private Environment env;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
	}
}
