package com.example.JavaCafe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.JavaCafe.ConfirmationToken;
import com.example.JavaCafe.User;
import com.example.JavaCafe.repository.ConfirmationTokenRepository;

@Service
public class ConfirmationTokenService {
	
	private final ConfirmationTokenRepository confirmationTokenRepository;
	private final EmailService emailService;
	@Autowired
	public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, EmailService emailService) {
		this.confirmationTokenRepository = confirmationTokenRepository;
		this.emailService = emailService;
	}

	public void saveConfirmationToken(ConfirmationToken token) {
		confirmationTokenRepository.save(token);
	}
	
	public Optional<ConfirmationToken> getToken(String token){
		return confirmationTokenRepository.findByToken(token);
	}
	
	public void sendConfirmationEmail(User user, ConfirmationToken token) {
		String subject = "Email Verification - JavaCafe";
		String text = "Hello " + user.getFirstName() + "\n\n\n" + 
				"Please click on the following link to verify your email and get started with us. \n" +
				"http://localhost:8082/users/confirm?token=" + token.getToken()+"\n\n"+
				"This link will expire in 24 hours.";
		emailService.sendMessage(user.getEmail(),subject,text);
		
	}
	
	
}
