package com.test.jwt.auth;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	private final SecretKey secretKey;
	private final Long accessExpiredMs;  //액세스 토큰 만료 시간
	private final Long refreshExpiredMs; //리프레쉬 토큰 만료 시간
	
	public JWTUtil(String secretKey, Long accessExpiredMs, Long refreshExpiredMs) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessExpiredMs = accessExpiredMs;
		this.refreshExpiredMs = refreshExpiredMs;		
	}

}