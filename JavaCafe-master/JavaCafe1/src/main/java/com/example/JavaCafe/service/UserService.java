package com.example.JavaCafe.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.JavaCafe.ConfirmationToken;
import com.example.JavaCafe.User;
import com.example.JavaCafe.Dto.UserDTO;
import com.example.JavaCafe.repository.UserRepository;
@Service
public class UserService {

	
	UserRepository userRepository;
	private final ConfirmationTokenService confirmationTokenService;
	@Autowired
	public UserService(UserRepository userRepository, ConfirmationTokenService confirmationTokenService) {
		this.confirmationTokenService = confirmationTokenService;
		this.userRepository = userRepository;
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
		if (user!= null && password.trim().equals(user.getPassword().trim())){
			if(user.isEmailVerified() == true) {
				return "Hello" + user.getFirstName();
				
			}else {
				return "Please verify your email first.";
			}
	}
		return "Invalid Credentials";
	}
	
}
