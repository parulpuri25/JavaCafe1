package com.example.JavaCafe.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.JavaCafe.ConfirmationToken;
import com.example.JavaCafe.User;
import com.example.JavaCafe.Dto.UserDTO;
import com.example.JavaCafe.repository.UserRepository;
import com.example.JavaCafe.service.ConfirmationTokenService;
import com.example.JavaCafe.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	private final ConfirmationTokenService confirmationTokenService;
	private final UserRepository userRepository;
	Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
	public UserController(UserService userService, ConfirmationTokenService confirmationTokenService, UserRepository userRepository) {
		this.userService = userService;
		this.confirmationTokenService = confirmationTokenService;
		this.userRepository = userRepository;
	}
	@PostMapping("/register")
    public ResponseEntity<String> registerUser(UserDTO userDto) {
        String message = userService.register(userDto);
        return ResponseEntity.ok(message);
    }
    
	@PostMapping("/login")
    public ResponseEntity<String> loginUser(
           String email,
             String password) {
        String message = userService.login(email, password);
        return ResponseEntity.ok(message);
}
	
	@GetMapping("/confirm")
	public ResponseEntity<String> confirmEmail(String token) {
		System.out.println("Received confirmation request with token: {}" + token);

		Optional<ConfirmationToken> optionalToken = confirmationTokenService.getToken(token);
		if(optionalToken.isPresent()) {
			ConfirmationToken confirmationToken = optionalToken.get();
			if(LocalDateTime.now().isBefore(confirmationToken.getExpiresAt())) {
				User user = confirmationToken.getUser();
				user.setEmailVerified(true);
				userRepository.save(user);
				return ResponseEntity.ok("Email Confirmed. Please proceed to Login Now!!");
			} else {
				return ResponseEntity.badRequest().body("The token has expired!!");
			}
		}
		else {
			return ResponseEntity.badRequest().body("Invalid Token");
		}
		
		
		
	}
}
