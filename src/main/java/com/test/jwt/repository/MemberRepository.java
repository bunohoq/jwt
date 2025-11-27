package com.test.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.jwt.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

}











