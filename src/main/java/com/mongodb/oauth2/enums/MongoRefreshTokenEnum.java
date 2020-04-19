package com.mongodb.oauth2.enums;

public enum MongoRefreshTokenEnum {

	TOKEN_ID("tokenId");
	
	String columnKey;
	
	MongoRefreshTokenEnum(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getColumnKey() {
		return columnKey;
	}
}
