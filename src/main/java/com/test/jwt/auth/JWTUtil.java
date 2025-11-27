package com.test.jwt.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	private final SecretKey secretKey;
	private final Long accessExpiredMs;  //액세스 토큰 만료 시간
	private final Long refreshExpiredMs; //리프레쉬 토큰 만료 시간
	
	public JWTUtil(
			@Value("${spring.jwt.secret}") String secretKey, 
			@Value("${spring.jwt.secret}") Long accessExpiredMs, 
			@Value("${spring.jwt.secret}") Long refreshExpiredMs) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessExpiredMs = accessExpiredMs;
		this.refreshExpiredMs = refreshExpiredMs;		
	}
	
	//JWT 문자열을 생성하는 메서드
	//- 인증 과정을 거친 후 생성된 JWT 문자열은 클라이언트에게 전달된다.
	public String createJwt(String username, String role, Long expiredMs) {
		
		//- claim() : 토큰의 페이로드에 사용자 정보를 저장
		//- issuedAt(): 토큰 생성 시각
		//- expiration(): 토큰 만료 시각
		//- signWith(): 서명(위변조 방지)
		//- compact(): header.payload.signature 형태의 최종 JWT 문자열 생성
		
		return Jwts.builder()
				.claim("username", username)
				.claim("role", role)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiredMs))
				.signWith(secretKey)
				.compact();
	}
	
	//클라이언트가 서버에 접속할 때 이전에 받은 JWT 토큰을 전달 + 요청
	//- 토큰 검증
	//- 토큰 파싱 > 정보 추
	private Claims getClaims(String token) {
		
		return Jwts.parser()
					.verifyWith(secretKey) 		//토큰이 올바른지 검증
					.build()
					.parseSignedClaims(token) 	//토큰 분해 > 각 정보를 페이로드에 담기
					.getPayload(); 				//정보를 페이로드에서 가져온 값을 Claims 담기
	}
	
    public String getUsername(String token) {
        return getClaims(token).get("username", String.class);
    }


    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    //JWT 토큰
    //1. 액세스 토큰
    //2. 리프레시 토큰
    
    public String createAccessToken(String username, String role) {
    	
    	return createJwt(username, role, accessExpiredMs);
    }
    
    public String createRefreshToken(String username, String role) {
    	return createJwt(username, role, refreshExpiredMs);
    }

}