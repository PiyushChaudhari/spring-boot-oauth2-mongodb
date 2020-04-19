package com.mongodb.oauth2.dao;

import java.util.List;

import org.springframework.security.oauth2.provider.ClientDetails;

import com.mongodb.oauth2.entity.MongoClientDetails;


public interface MongoClientDetailsDao {

	public MongoClientDetails loadClientByClientId(String clientId);
	
	public void addClientDetails(ClientDetails clientDetails);
	
	public long updateClientDetails(ClientDetails clientDetails);
	
	public long updateClientSecret(String clientId, String clientSecret);
	
	public long removeClientDetails(String clientId);
	
	List<ClientDetails> listClientDetails();
}
