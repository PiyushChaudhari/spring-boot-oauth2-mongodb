package com.mongodb.oauth2.dao;

import java.util.Collection;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.mongodb.oauth2.entity.MongoAccessToken;
import com.mongodb.oauth2.entity.MongoRefreshToken;


public interface MongoTokenStoreDao {

	public OAuth2Authentication readAuthentication(String token);
	
	public void storeAccessToken(MongoAccessToken accessToken);
	
	public MongoAccessToken readAccessToken(String tokenValue);
	
	public long removeAccessToken(OAuth2AccessToken oAuth2AccessToken);
	
	public void storeRefreshToken(MongoRefreshToken refreshToken);
	
	public MongoRefreshToken readRefreshToken(String tokenValue);
	
	public MongoRefreshToken readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken);
	
	public long removeRefreshToken(OAuth2RefreshToken refreshToken);
	
	public long removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken);
	
	public MongoAccessToken getAccessToken(String authenticationId);
	
	public Collection<MongoAccessToken> findTokensByClientIdAndUserName(String clientId, String username);
	
	public Collection<MongoAccessToken> findTokensByClientId(String clientId);
}
