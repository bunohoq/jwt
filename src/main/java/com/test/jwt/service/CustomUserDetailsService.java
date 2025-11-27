package com.test.jwt.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.test.jwt.dto.CustomUserDetails;
import com.test.jwt.entity.Member;
import com.test.jwt.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	
	private final MemberRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<Member> member = repo.findByUsername(username);
		
		if (member.isPresent()) {
			
			//로그인 성공 > UserDetails 객체 생성
			return new CustomUserDetails(member.get());
		} else {
			
			//로그인 실패 > 시큐리티 권장 > 예외 발생
			throw new UsernameNotFoundException("로그인 실패" + username);
		}
		
	}
	

}
