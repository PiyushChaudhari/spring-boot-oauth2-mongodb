package com.mongodb.oauth2.enums;

public enum MongoAccessTokenEnum {

	TOKEN_ID("tokenId"),
	REFRESH_TOKEN("refreshToken"),
    AUTHENTICATION_ID("authenticationId"),
    CLIENT_ID("clientId"),
    USERNAME("username");
	
	String columnKey;
	
	MongoAccessTokenEnum(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getColumnKey() {
		return columnKey;
	}
}
