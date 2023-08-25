package com.example.JavaCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.JavaCafe.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long >{
	
	User findByEmail(String email);
	User getUserDetailsByEmail(String email);

}
