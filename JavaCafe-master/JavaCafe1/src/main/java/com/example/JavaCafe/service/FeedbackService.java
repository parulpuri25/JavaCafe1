package com.example.JavaCafe.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.JavaCafe.Feedback;
import com.example.JavaCafe.repository.FeedbackRepository;
@Service
public class FeedbackService {
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	@Autowired
	private EmailService emailService;
	
	public void saveFeedback(Feedback feedback) {
		int referenceNumber = generateRandomNumber(100000,999999);
		feedback.setReferenceNumber(referenceNumber);
		feedbackRepository.save(feedback);
		
	}
	public void sendAcknowledgementMessage(Feedback feedback) {
		String subject = "Feedback Received - Reference#" + feedback.getReferenceNumber();
		String message = "Thank you for submitting your feedback!! \n\n"+
		"Your reference number is "+ feedback.getReferenceNumber()+
		"\n. A member of our team will reach out to you in 3-5 business days.";
		
		emailService.sendMessage(feedback.getEmail(), subject, message);
	}
	private int generateRandomNumber(int min, int max) {
		Random random = new Random();
		return random.nextInt((max - min)+1)+min;
	}
}
