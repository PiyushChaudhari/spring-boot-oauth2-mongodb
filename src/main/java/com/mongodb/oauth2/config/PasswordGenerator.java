package com.mongodb.oauth2.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

	public static void main(String[] args) {

		System.out.println(new BCryptPasswordEncoder().encode("@uTh$CATaloGue@BB"));
		System.out.println(new BCryptPasswordEncoder().encode("bB$cHuB@tOkEn"));
	}

}
