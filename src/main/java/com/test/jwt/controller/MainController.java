package com.test.jwt.controller;

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















