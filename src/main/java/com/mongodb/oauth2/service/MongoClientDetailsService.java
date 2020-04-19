package com.mongodb.oauth2.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import com.mongodb.oauth2.dao.MongoClientDetailsDao;
import com.mongodb.oauth2.entity.MongoClientDetails;

public class MongoClientDetailsService implements ClientDetailsService, ClientRegistrationService{
	
	Logger log = LoggerFactory.getLogger(MongoClientDetailsService.class);
		
	@Autowired
    private MongoClientDetailsDao clientDetailsDao;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId){
		
		log.info("loadClientByClientId clientId:> {}",clientId);
		
        MongoClientDetails clientDetails = clientDetailsDao.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new NoSuchClientException(String.format("Client with id %s not found.", clientId));
        }
        return clientDetails;
	}

	@Override
	public void addClientDetails(ClientDetails clientDetails){
		log.info("addClientDetails:> {}",clientDetails);
		MongoClientDetails clientDetailsExist = clientDetailsDao.loadClientByClientId(clientDetails.getClientId());
		if(null == clientDetailsExist) {
			clientDetailsDao.addClientDetails(clientDetailsExist);
		}else {
			throw new ClientAlreadyExistsException(String.format("Client already exists with id %s ",clientDetails.getClientId()));
		}
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails){
		log.info("updateClientDetails:> {}",clientDetails);
		long updateClientCount = clientDetailsDao.updateClientDetails(clientDetails);
		log.info("updateClientCount:> {}",updateClientCount);
		if(updateClientCount <= 0) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientDetails.getClientId()));
        }
	}

	@Override
	public void updateClientSecret(String clientId, String secret){
		log.info("updateClientSecret clientId:> {} and secret:> {}",clientId,secret);
		long updateClientSecretCount = clientDetailsDao.updateClientSecret(clientId, secret);
		log.info("updateClientSecretCount:> {}",updateClientSecretCount);
		if(updateClientSecretCount <= 0) {
			throw new NoSuchClientException(String.format("Client with id %s not found", clientId));
        }
	}

	@Override
	public void removeClientDetails(String clientId){
		log.info("removeClientDetails clientId:> {}",clientId);
		long removeClientDetailsCount = clientDetailsDao.removeClientDetails(clientId);
		log.info("removeClientDetailsCount:> {}",removeClientDetailsCount);
		if(removeClientDetailsCount <= 0) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientId));
        }
	}

	@Override
	public List<ClientDetails> listClientDetails() {
		List<ClientDetails> clientList = clientDetailsDao.listClientDetails();
		log.info("listClientDetails total count:> {}",clientList.size());
		return clientList;
	}	
}
