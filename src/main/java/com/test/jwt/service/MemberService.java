package com.test.jwt.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.jwt.dto.MemberDTO;
import com.test.jwt.entity.Member;
import com.test.jwt.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository repo;
	private final BCryptPasswordEncoder encoder;

	public void join(MemberDTO dto) {
		
		Member member = Member.builder()
						.username(dto.getUsername())
						.password(encoder.encode(dto.getPassword()))
						.role(dto.getRole())
						.build();
		
		repo.save(member);
	}

}











