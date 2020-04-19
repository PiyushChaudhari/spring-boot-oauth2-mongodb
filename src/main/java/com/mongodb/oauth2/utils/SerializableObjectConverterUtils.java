package com.mongodb.oauth2.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class SerializableObjectConverterUtils {

	private SerializableObjectConverterUtils() {}
	
    public static String serialize(OAuth2Authentication object) {
        byte[] bytes = MongoSerializationUtils.serialize(object);
        return Base64.encodeBase64String(bytes);
    }

    public static OAuth2Authentication deserialize(String encodedObject) {
        byte[] bytes = Base64.decodeBase64(encodedObject);
        return (OAuth2Authentication) MongoSerializationUtils.deserialize(bytes);
    }

}