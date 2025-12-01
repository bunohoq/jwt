package com.test.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.test.jwt.auth.JWTFilter;
import com.test.jwt.auth.JWTUtil;
import com.test.jwt.auth.LoginFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final AuthenticationConfiguration configuration;
	private final JWTUtil jwtUtil;

	@Bean
	BCryptPasswordEncoder encoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		
		//CORS 설정
		http.cors(auth -> auth.configurationSource(corsConfigurationSource()));
		
		
		//CSRF 비활성
		http.csrf(auth -> auth.disable());
		
		//Form Login > 사용안함
		http.formLogin(auth -> auth.disable());
		
		//기본 인증
		http.httpBasic(auth -> auth.disable());
		
		//세션 설정
		//- 기존의 세션 인증 방식을 비활성화 > 대신 JWT 방식 사용
		http.sessionManagement(auth -> auth
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);
		
		//허가 URL
		http.authorizeHttpRequests(auth -> auth
			.requestMatchers("/", "/login/**", "join/**", "/joinok/**").permitAll()
			.requestMatchers("/member/**").hasAnyRole("MEMBER", "ADMIN")
			.requestMatchers("/admin").hasRole("ADMIN")
			.anyRequest().authenticated()
		);
		
		//로그아웃 추가
//		http.logout(auth -> auth
//			.logoutUrl("/logout")
//			.logoutSuccessUrl("/")
//			.invalidateHttpSession(true)
//			.deleteCookies("JSESSIONID")
//		);
		
		
		//필터 작성 순서는 의미없다.
		
		//JWTFilter 등록하기
		http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
				
		//LoginFilter 등록하기
		//- /login 요청 > 이 필터 가로채어서 동작을 한다.
		//- UsernamePasswordAuthenticationFilter(시큐리티 기본 인증 필터) > LoginFilter(사용자 정의)로 교체
		http.addFilterAt(new LoginFilter(manager(configuration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	
	//AuthenticationManager
	//- JWT 인증 관리
	//- 사용자가 로그인 시도 > 실제로 ID와 PW가 일치하는 검증
	//- loadUserByUsername 관여
	//- 직접 생성 이유 > 폼 인증을 사용 안해서..
	
	//폼 인증 = ID/PW 입력 > Security가 자동 처리 + 세션 기반 인증
	//JWT 인증 = ID/PW 입력 > 커스텀 필터(LoginFilter) 동작 > AuthenticationManager 직접 인증 처리 > JWT 토큰 직접 발급 + 응답 헤더 반환 > 클라이언트는 응답 토큰 보관 > 이 후에 서버로 접속 + 토큰 전달 > 커스텀 필터(JWTFilter) 동작 + 토큰 유효성/검증
	@Bean
	AuthenticationManager manager(AuthenticationConfiguration config) throws Exception {
		
		return config.getAuthenticationManager();		
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:8081");

        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        source.registerCorsConfiguration("/**", config);
        
        return source;
    }
	
}
