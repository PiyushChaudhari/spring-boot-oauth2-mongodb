package com.mongodb.oauth2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;


@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	Logger log = LoggerFactory.getLogger(ResourceServerConfig.class);
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
        http
		    .anonymous().disable()
		    .authorizeRequests()
		    .antMatchers("/**").authenticated()
		    .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
//		    .authenticationEntryPoint(new CustomerhubOAuth2AuthenticationEntryPoint())
		    ;
        
	}
}
