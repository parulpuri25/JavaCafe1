package com.example.JavaCafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.JavaCafe.Feedback;
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{

}
