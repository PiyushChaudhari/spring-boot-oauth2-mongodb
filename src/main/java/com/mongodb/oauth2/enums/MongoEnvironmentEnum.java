package com.mongodb.oauth2.enums;

public enum MongoEnvironmentEnum {

	DEVELOPMENT("dev"),
	PRODUCTION("prod"),
	QA("qa");
	
	String details;
	
	MongoEnvironmentEnum(String details) {
		this.details = details;
	}

	public String getDetails() {
		return details;
	}
}
