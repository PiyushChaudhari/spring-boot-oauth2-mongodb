package com.mongodb.oauth2.config;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.mongodb.oauth2.service.MongoClientDetailsService;
import com.mongodb.oauth2.service.MongoTokenStoreService;



@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	Logger log = LoggerFactory.getLogger(AuthorizationServerConfig.class);

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	@Qualifier("secureUserDetailsService")
	UserDetailsService userDetailsService;
	
	
	@Bean
    public MongoClientDetailsService clientDetailsService() {
        return new MongoClientDetailsService();
    }
	
	@Bean
    public TokenStore tokenStore() {
        return new MongoTokenStoreService();
    }
	
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    	oauthServer
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()")
	    	.allowFormAuthenticationForClients()
	    	;
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .tokenStore(tokenStore())
            .userDetailsService(userDetailsService)
            .authenticationManager(authenticationManager)
            .pathMapping("/oauth/token", "/v1/generateToken")
//            .exceptionTranslator(exception -> {
//        	
//	            if (exception instanceof OAuth2Exception) {
//	                OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
//	                return ResponseEntity
//	                        .status(oAuth2Exception.getHttpErrorCode())
//	                        .body(new CustomOAuth2Exception(oAuth2Exception.getMessage()));
//	            } else {
//	                throw exception;
//	            }
//	        })
            ;
    }
}
