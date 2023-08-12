package com.example.JavaCafe.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.JavaCafe.ConfirmationToken;
import com.example.JavaCafe.Feedback;
import com.example.JavaCafe.ResetPasswordToken;
import com.example.JavaCafe.User;
import com.example.JavaCafe.Dto.UserDTO;
import com.example.JavaCafe.repository.UserRepository;

@Service
public class UserService {

	UserRepository userRepository;
	private final ConfirmationTokenService confirmationTokenService;
	private final ResetPasswordService resetPasswordService;
	private final FeedbackService feedbackService;

	@Autowired
	public UserService(UserRepository userRepository, ConfirmationTokenService confirmationTokenService,
			ResetPasswordService resetPasswordService, FeedbackService feedbackService) {
		this.confirmationTokenService = confirmationTokenService;
		this.userRepository = userRepository;
		this.resetPasswordService = resetPasswordService;
		this.feedbackService = feedbackService;
	}

	public String register(UserDTO userDto) {
		User user = new User();
		User userRepo = userRepository.findByEmail(userDto.getEmail());
		if (userRepo != null) {
			return "Error: Please use another email address." + userDto.getEmail();
		}
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		userRepository.save(user);

		String token = UUID.randomUUID().toString();

		ConfirmationToken confirmationToken = new ConfirmationToken();
		confirmationToken.setToken(token);
		confirmationToken.setUser(user);
		confirmationToken.setCreatedAt(LocalDateTime.now());
		confirmationToken.setExpiresAt(LocalDateTime.now().plusDays(1));

		confirmationTokenService.saveConfirmationToken(confirmationToken);
		confirmationTokenService.sendConfirmationEmail(user, confirmationToken);
		return "Registration is Successful. Next step is verify your email!!";
	}

	public String login(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user != null && password.trim().equals(user.getPassword().trim())) {
			if (user.isEmailVerified() == true) {
				return "Hello" + user.getFirstName();

			} else {
				return "Please verify your email first.";
			}
		}
		return "Invalid Credentials";
	}

	public String emailValidation(String token) {
		Optional<ConfirmationToken> optionalToken = confirmationTokenService.getToken(token);
		if (optionalToken.isPresent()) {
			ConfirmationToken confirmationToken = optionalToken.get();
			if (LocalDateTime.now().isBefore(confirmationToken.getExpiresAt())) {
				User user = confirmationToken.getUser();
				user.setEmailVerified(true);
				userRepository.save(user);
				return "Email Confirmed. Please proceed to Login Now!!";
			} else {
				return "The token has expired!!";
			}
		} else {
			return "Invalid Token";
		}
	}

	public String resetPasswordTokenGeneration(UserDTO userDto) {
		User user = userRepository.findByEmail(userDto.getEmail());
		if (user != null) {
			String passwordToken = UUID.randomUUID().toString();

			ResetPasswordToken resetPasswordToken = new ResetPasswordToken(passwordToken, null, null, user);
			resetPasswordToken.setEmailToken(passwordToken);
			resetPasswordToken.setCreatedAt(LocalDateTime.now());
			resetPasswordToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
			resetPasswordToken.setUser(user);

			resetPasswordService.saveResetPasswordToken(resetPasswordToken);
			resetPasswordService.sendResetLink(user, resetPasswordToken);

			return "An email with reset Instructions has been sent to you!!";
		}

		return "Invalid User";
	}
	
	public String resetPassword(String token, 
            String password) {
		Optional<ResetPasswordToken> optionalToken = resetPasswordService.getResetPasswordToken(token);
	    if (optionalToken.isPresent()) {
	        ResetPasswordToken resetPasswordToken1 = optionalToken.get();
	        if (LocalDateTime.now().isBefore(resetPasswordToken1.getExpiresAt())) {
	            User user = resetPasswordToken1.getUser();
	            user.setPassword(password);
	            System.out.println(password+"password");
	            userRepository.save(user);       
	            return "Password has been changed. Please proceed to login.";
	        } else {
	            return "The token has expired.";
	        }
	    } else {
	        return "Invalid token.";
	    }
	    }
	public String submitFeedback(Feedback feedback) {
		feedbackService.saveFeedback(feedback);
		feedbackService.sendAcknowledgementMessage(feedback);
		return "Feedback has been submitted " + feedback.getFirstName();
	}
}
