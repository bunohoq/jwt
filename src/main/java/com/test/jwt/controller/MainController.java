package com.test.jwt.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.jwt.dto.MemberDTO;
import com.test.jwt.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainController {

	private final MemberService service;
	
	@GetMapping("/")
	public String index() {
		
		return "MainController >>>>> /index";
	}
	
	@GetMapping("/member")
	public String member() {
		
		return "MainController >>>>> /member";
	}
	

	@GetMapping(value="/member/info")
    public ResponseEntity<?> getMemberInfo() {
		
		//REST Server 응답 데이터
		//1. 문자열 or JSON
		//2. 1 + 상태코드(200, 500)
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
        }

        String username = authentication.getName();

        String role = authentication.getAuthorities().stream()
                            .findFirst()
                            .map(GrantedAuthority::getAuthority)
                            .orElse("UNKNOWN");

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("role", role);

        //userInfo > JSON 생성
        //상태코드 > 200
        return ResponseEntity.ok(userInfo);
    }
	
	@GetMapping("/admin")
	public String admin() {
		
		return "MainController >>>>> /admin";
	}

	@PostMapping("/joinok")
	public String joinok(MemberDTO dto) {
		
		System.out.println("MemberDTO >>>>> " + dto);
		service.join(dto);
		
		return "MainController >>>>> /joinok";
	}
	
}















