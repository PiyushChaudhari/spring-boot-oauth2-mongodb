package com.mongodb.oauth2.dao;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.oauth2.entity.MongoAccessToken;
import com.mongodb.oauth2.entity.MongoRefreshToken;
import com.mongodb.oauth2.enums.MongoAccessTokenEnum;
import com.mongodb.oauth2.enums.MongoRefreshTokenEnum;
import com.mongodb.oauth2.utils.TokenUtils;

@Repository
public class MongoTokenStoreDaoImpl implements MongoTokenStoreDao {

    @Autowired
    private MongoTemplate mongoTemplate;
	
	@Override
	public OAuth2Authentication readAuthentication(String token) {		
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessTokenEnum.TOKEN_ID.getColumnKey()).is(TokenUtils.extractTokenKey(token)));
        MongoAccessToken customerhubAccessToken = mongoTemplate.findOne(query, MongoAccessToken.class);
        return customerhubAccessToken != null ? customerhubAccessToken.getAuthentication() : null;
	}

	@Override
	public void storeAccessToken(MongoAccessToken accessToken) {
		mongoTemplate.save(accessToken);
	}

	@Override
	public MongoAccessToken readAccessToken(String tokenValue) {
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessTokenEnum.TOKEN_ID.getColumnKey()).is(TokenUtils.extractTokenKey(tokenValue)));
        return mongoTemplate.findOne(query, MongoAccessToken.class);
	}

	@Override
	public long removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessTokenEnum.TOKEN_ID.getColumnKey()).is(TokenUtils.extractTokenKey(oAuth2AccessToken.getValue())));
        DeleteResult removeToken=mongoTemplate.remove(query, MongoAccessToken.class);
		return removeToken.getDeletedCount();
	}

	@Override
	public void storeRefreshToken(MongoRefreshToken refreshToken) {
		mongoTemplate.save(refreshToken);
	}

	@Override
	public MongoRefreshToken readRefreshToken(String tokenValue) {
		 Query query = new Query();
	     query.addCriteria(Criteria.where(MongoRefreshTokenEnum.TOKEN_ID.getColumnKey()).is(TokenUtils.extractTokenKey(tokenValue)));
		return mongoTemplate.findOne(query, MongoRefreshToken.class);
	}

	@Override
	public MongoRefreshToken readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoRefreshTokenEnum.TOKEN_ID.getColumnKey()).is(TokenUtils.extractTokenKey(refreshToken.getValue())));
		return mongoTemplate.findOne(query, MongoRefreshToken.class);
	}
	
	@Override
	public long removeRefreshToken(OAuth2RefreshToken refreshToken) {
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoRefreshTokenEnum.TOKEN_ID.getColumnKey()).is(TokenUtils.extractTokenKey(refreshToken.getValue())));
        DeleteResult removeRefreshToken = mongoTemplate.remove(query, MongoRefreshToken.class);
		return removeRefreshToken.getDeletedCount();
	}

	@Override
	public long removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessTokenEnum.REFRESH_TOKEN.getColumnKey()).is(TokenUtils.extractTokenKey(refreshToken.getValue())));
        DeleteResult removeAccessTokenUsingRefreshToken= mongoTemplate.remove(query, MongoAccessToken.class);
		return removeAccessTokenUsingRefreshToken.getDeletedCount();
	}
	
	@Override
	public MongoAccessToken getAccessToken(String authenticationId) {
		
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessTokenEnum.AUTHENTICATION_ID.getColumnKey()).is(authenticationId));
		return mongoTemplate.findOne(query, MongoAccessToken.class);
	}
	
	@Override
	public Collection<MongoAccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessTokenEnum.CLIENT_ID.getColumnKey()).is(clientId));
        query.addCriteria(Criteria.where(MongoAccessTokenEnum.USERNAME.getColumnKey()).is(username));
		return mongoTemplate.find(query, MongoAccessToken.class);
	}
	
	@Override
	public Collection<MongoAccessToken> findTokensByClientId(String clientId) {
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessTokenEnum.CLIENT_ID.getColumnKey()).is(clientId));
		return mongoTemplate.find(query, MongoAccessToken.class);
	}
}
