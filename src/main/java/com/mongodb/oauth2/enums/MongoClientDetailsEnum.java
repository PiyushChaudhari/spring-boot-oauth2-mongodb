package com.mongodb.oauth2.enums;

public enum MongoClientDetailsEnum {

	CLIENT_ID("clientId"),
	CLIENT_SECRET("clientSecret"),
	SECRET_REQUIRED("secretRequired"),
	RESOURCE_IDS("resourceIds"),
	SCOPED("scoped"),
	SCOPE("scope"),	
	AUTHORIZED_GRANT_TYPES("authorizedGrantTypes"),
	REGISTERED_REDIRECT_URI("registeredRedirectUri"),
	AUTHORITIES("authorities"),
	ACCESS_TOKEN_VALIDITY_SECONDS("accessTokenValiditySeconds"),
	REFRESH_TOKEN_VALIDITY_SECONDS("refreshTokenValiditySeconds"),
	AUTO_APPROVE("autoApprove"),
	ADDITIONAL_INFORMATION("additionalInformation");

	String columnKey;


	MongoClientDetailsEnum(String columnKey) {
		this.columnKey = columnKey;
	}
	
	public String getColumnKey() {
		return columnKey;
	}
}
