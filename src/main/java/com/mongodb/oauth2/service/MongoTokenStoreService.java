package com.mongodb.oauth2.service;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.mongodb.oauth2.dao.MongoTokenStoreDao;
import com.mongodb.oauth2.entity.MongoAccessToken;
import com.mongodb.oauth2.entity.MongoRefreshToken;
import com.mongodb.oauth2.utils.TokenUtils;

public class MongoTokenStoreService implements TokenStore{
	
	Logger log = LoggerFactory.getLogger(MongoTokenStoreService.class);
	
	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
	
	@Autowired
	private MongoTokenStoreDao tokenStoreDao;

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
		log.info("readAuthentication accessToken:> {}",accessToken);
		return readAuthentication(accessToken.getValue());
	}

	@Override
	public OAuth2Authentication readAuthentication(String token) {
		log.info("readAuthentication token:> {}",token);
		return tokenStoreDao.readAuthentication(token);
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		log.info("storeAccessToken accessToken:> {}",accessToken);
		log.info("storeAccessToken authentication:> {}",authentication);
		String refreshToken = null;
        if (accessToken.getRefreshToken() != null) {
            refreshToken = accessToken.getRefreshToken().getValue();
        }
        
        if (readAccessToken(accessToken.getValue()) != null) {
            this.removeAccessToken(accessToken);
        }
        
        MongoAccessToken token = new MongoAccessToken();
        
        token.setTokenId(TokenUtils.extractTokenKey(accessToken.getValue()));
        token.setToken(accessToken);
        token.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        token.setUsername(authentication.isClientOnly() ? null : authentication.getName());
        token.setClientId(authentication.getOAuth2Request().getClientId());
        token.setAuthentication(authentication);
        token.setRefreshToken(TokenUtils.extractTokenKey(refreshToken));
        tokenStoreDao.storeAccessToken(token);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		log.info("readAccessToken tokenValue:> {}",tokenValue);
		MongoAccessToken accessToken = tokenStoreDao.readAccessToken(tokenValue);
		return accessToken != null ? accessToken.getToken() : null;
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
		log.info("removeAccessToken oAuth2AccessToken:> {}",oAuth2AccessToken);
		long removeCount = tokenStoreDao.removeAccessToken(oAuth2AccessToken);
		log.info("removeAccessToken removeCount:> {}",removeCount);
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		log.info("storeRefreshToken refreshToken:> {}",refreshToken);
		log.info("storeRefreshToken authentication:> {}",authentication);
		
		MongoRefreshToken refreshTokenInfo = new MongoRefreshToken();
		refreshTokenInfo.setTokenId(TokenUtils.extractTokenKey(refreshToken.getValue()));
		refreshTokenInfo.setToken(refreshToken);
		refreshTokenInfo.setAuthentication(authentication);
		tokenStoreDao.storeRefreshToken(refreshTokenInfo);
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		log.info("readRefreshToken tokenValue:> {}",tokenValue);
		MongoRefreshToken refreshToken = tokenStoreDao.readRefreshToken(tokenValue);
		return refreshToken != null ? refreshToken.getToken() : null;
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {
		log.info("readAuthenticationForRefreshToken refreshToken:> {}",refreshToken);
		MongoRefreshToken refreshTokenDetails = tokenStoreDao.readAuthenticationForRefreshToken(refreshToken);
		return refreshTokenDetails != null ? refreshTokenDetails.getAuthentication() : null;
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		log.info("removeRefreshToken token:> {}",token);
		long removeRefreshTokenCount =tokenStoreDao.removeRefreshToken(token);
		log.info("removeRefreshToken removeRefreshTokenCount:> {}",removeRefreshTokenCount);
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		log.info("removeAccessTokenUsingRefreshToken refreshToken:> {}",refreshToken);
		long removeAccessTokenUsingRefreshTokenCount =tokenStoreDao.removeAccessTokenUsingRefreshToken(refreshToken);
		log.info("removeAccessTokenUsingRefreshToken remove count:> {}",removeAccessTokenUsingRefreshTokenCount);
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		log.info("getAccessToken authentication:> {}",authentication);
		OAuth2AccessToken accessToken = null;
        String authenticationId = authenticationKeyGenerator.extractKey(authentication);
        MongoAccessToken accessTokenDetails = tokenStoreDao.getAccessToken(authenticationId);
        if (accessTokenDetails != null) {
            accessToken = accessTokenDetails.getToken();
            if(accessToken != null && !authenticationId.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken)))) {
                this.removeAccessToken(accessToken);
                this.storeAccessToken(accessToken, authentication);
            }
        }
		return accessToken;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
		log.info("findTokensByClientIdAndUserName clientId:> {} , username",clientId,username);
		Collection<OAuth2AccessToken> tokens = new ArrayList<>();
        Collection<MongoAccessToken> accessTokens = tokenStoreDao.findTokensByClientIdAndUserName(clientId, username);
        for (MongoAccessToken accessToken : accessTokens) {
            tokens.add(accessToken.getToken());
        }
        return tokens;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		log.info("findTokensByClientId clientId:> {} ",clientId);
		Collection<OAuth2AccessToken> tokens = new ArrayList<>();
		Collection<MongoAccessToken> accessTokens = tokenStoreDao.findTokensByClientId(clientId);
		for (MongoAccessToken accessToken : accessTokens) {
            tokens.add(accessToken.getToken());
        }
		return tokens;
	}

}
