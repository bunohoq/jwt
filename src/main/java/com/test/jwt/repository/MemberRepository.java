package com.test.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.jwt.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

	Optional<Member> findByUsername(String username);

}











