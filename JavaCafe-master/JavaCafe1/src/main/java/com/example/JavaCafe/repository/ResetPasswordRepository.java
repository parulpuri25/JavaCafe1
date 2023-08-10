package com.example.JavaCafe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.JavaCafe.ResetPasswordToken;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPasswordToken, Long>{
	Optional<ResetPasswordToken> findByEmailToken(String emailToken);
}
