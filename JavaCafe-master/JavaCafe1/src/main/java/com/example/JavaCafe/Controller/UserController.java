package com.example.JavaCafe.Controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.JavaCafe.Feedback;
import com.example.JavaCafe.ResetPasswordToken;
import com.example.JavaCafe.Dto.UserDTO;
import com.example.JavaCafe.repository.UserRepository;
import com.example.JavaCafe.service.ResetPasswordService;
import com.example.JavaCafe.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	private final ResetPasswordService resetPasswordService;
	Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	public UserController(UserService userService, ResetPasswordService resetPasswordService,
			UserRepository userRepository) {
		this.userService = userService;
		this.resetPasswordService = resetPasswordService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(UserDTO userDto) {
		String message = userService.register(userDto);
		return ResponseEntity.ok(message);
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(String email, String password) {
		String message = userService.login(email, password);
		return ResponseEntity.ok(message);
	}

	@GetMapping("/confirm")
	public ResponseEntity<String> confirmEmail(String token) {
		String message = userService.emailValidation(token);
		return ResponseEntity.ok(message);
	}
	
	@PostMapping("/forgotPassword")
	public ResponseEntity<String> sendResetLink(UserDTO userDto){
		String message = userService.resetPasswordTokenGeneration(userDto);
		return ResponseEntity.ok(message);
	}
	
	@GetMapping("/resetPassword")
	public ResponseEntity<String> showResetPasswordForm(@RequestParam("resetPasswordToken") String resetPasswordToken, 
			UserDTO userDto, Model model) {
	    Optional<ResetPasswordToken> optionalToken = resetPasswordService.getResetPasswordToken(resetPasswordToken);
	    System.out.println(optionalToken);    
	    if (optionalToken.isPresent()) {
	    	ResetPasswordToken resetPasswordToken1 = optionalToken.get();
	        String extractedToken = resetPasswordToken1.getEmailToken();
	        System.out.println(extractedToken);
	        model.addAttribute("resetPasswordToken",extractedToken);
	    	return ResponseEntity.status(302)
	                .header("Location", "/resetPassword.html?resetPasswordToken="+extractedToken)
	                .build();
	    } else {
	    	return null;	        
	    }
	}
	
	@PostMapping("/resetPassword")
	public ResponseEntity<String> processResetPassword(String token, 
	                                                   String password) {
	    String message = userService.resetPassword(token, password);
	    return ResponseEntity.ok(message);
	}

	@PostMapping("/submitFeedback")
	public ResponseEntity<String> submitFeedback(Feedback feedback) {
	    String message = userService.submitFeedback(feedback);
	    return ResponseEntity.ok(message);
	}

	
}
