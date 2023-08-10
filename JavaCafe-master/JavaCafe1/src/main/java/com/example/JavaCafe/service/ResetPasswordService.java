package com.example.JavaCafe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.JavaCafe.ResetPasswordToken;
import com.example.JavaCafe.User;
import com.example.JavaCafe.repository.ResetPasswordRepository;
@Service
public class ResetPasswordService {
	
	private final ResetPasswordRepository resetPasswordRepository;
	private final EmailService emailService;
	@Autowired
	public ResetPasswordService(ResetPasswordRepository resetPasswordRepository, EmailService emailService) {
		super();
		this.resetPasswordRepository = resetPasswordRepository;
		this.emailService = emailService;
	}
	
	public void saveResetPasswordToken(ResetPasswordToken resetPasswordToken) {
		resetPasswordRepository.save(resetPasswordToken);
	}
	
	public Optional<ResetPasswordToken> getResetPasswordToken(String resetPasswordToken){
		return resetPasswordRepository.findByEmailToken(resetPasswordToken);
	}
	
	public void sendResetLink(User user, ResetPasswordToken resetPasswordToken) {
		String subject = "Password Reset Link - JavaCafe";
		String text = "Hello " + user.getFirstName()+ "\n\n"+
						"Please follow the link below to reset your password. \n"+
						"http://localhost:8082/users/resetPassword?resetPasswordToken=" + 
						resetPasswordToken.getEmailToken() + "\n\n" +
						"The link will expire in 15 minutes";

		emailService.sendMessage(user.getEmail(), subject, text);
	}

}
