package com.test.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.jwt.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String>{

}
