package com.mongodb.oauth2.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.oauth2.entity.MongoClientDetails;
import com.mongodb.oauth2.enums.MongoClientDetailsEnum;

@Repository
public class MongoClientDetailsDaoImpl implements MongoClientDetailsDao{
	
	Logger log = LoggerFactory.getLogger(MongoClientDetailsDaoImpl.class);

	@Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public MongoClientDetails loadClientByClientId(String clientId) {
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoClientDetailsEnum.CLIENT_ID.getColumnKey()).is(clientId));
		return mongoTemplate.findOne(query, MongoClientDetails.class);
	}

	@Override
	public void addClientDetails(ClientDetails clientDetails) {
		mongoTemplate.save(clientDetails);
	}

	@Override
	public long updateClientDetails(ClientDetails clientDetails) {
		
        Query query = new Query();
        query.addCriteria(Criteria.where(MongoClientDetailsEnum.CLIENT_ID.getColumnKey()).is(clientDetails.getClientId()));

        Update update = new Update();
        update.set(MongoClientDetailsEnum.RESOURCE_IDS.getColumnKey(), clientDetails.getResourceIds());
        update.set(MongoClientDetailsEnum.SCOPE.getColumnKey(), clientDetails.getScope());
        update.set(MongoClientDetailsEnum.AUTHORIZED_GRANT_TYPES.getColumnKey(), clientDetails.getAuthorizedGrantTypes());
        update.set(MongoClientDetailsEnum.REGISTERED_REDIRECT_URI.getColumnKey(), clientDetails.getRegisteredRedirectUri());
        update.set(MongoClientDetailsEnum.AUTHORITIES.getColumnKey(), clientDetails.getAuthorities());
        update.set(MongoClientDetailsEnum.ACCESS_TOKEN_VALIDITY_SECONDS.getColumnKey(), clientDetails.getAccessTokenValiditySeconds());
        update.set(MongoClientDetailsEnum.REFRESH_TOKEN_VALIDITY_SECONDS.getColumnKey(), clientDetails.getRefreshTokenValiditySeconds());
        update.set(MongoClientDetailsEnum.ADDITIONAL_INFORMATION.getColumnKey(), clientDetails.getAdditionalInformation());

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MongoClientDetails.class);
		
		return updateResult.getModifiedCount();
	}

	@Override
	public long updateClientSecret(String clientId, String clientSecret) {
		
        Query query = new Query();
        query.addCriteria(Criteria.where(MongoClientDetailsEnum.CLIENT_ID.getColumnKey()).is(clientId));

        Update update = new Update();
        update.set(MongoClientDetailsEnum.CLIENT_SECRET.getColumnKey(),clientSecret);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MongoClientDetails.class);
        return updateResult.getModifiedCount();
	}

	@Override
	public long removeClientDetails(String clientId) {
		Query query = new Query();
        query.addCriteria(Criteria.where(MongoClientDetailsEnum.CLIENT_ID.getColumnKey()).is(clientId));
        DeleteResult deleteResult = mongoTemplate.remove(query, MongoClientDetails.class);
		return deleteResult.getDeletedCount();
	}

	@Override
	public List<ClientDetails> listClientDetails() {
		List<ClientDetails> result =  new ArrayList<>();
        List<MongoClientDetails> details = mongoTemplate.findAll(MongoClientDetails.class);
        for (MongoClientDetails detail : details) {
            result.add(detail);
        }
        return result;
	}
}
