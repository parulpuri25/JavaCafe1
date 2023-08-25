package com.example.JavaCafe.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.JavaCafe.Feedback;
import com.example.JavaCafe.ResetPasswordToken;
import com.example.JavaCafe.User;
import com.example.JavaCafe.Dto.UserDTO;
import com.example.JavaCafe.repository.UserRepository;
import com.example.JavaCafe.service.ResetPasswordService;
import com.example.JavaCafe.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
public class UserController {
	private static boolean isLoggedIn = false;
	//@Autowired
	//private SimpMessagingTemplate simpMessagingTemplate;
	private final UserService userService;
	private final ResetPasswordService resetPasswordService;
	Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	public UserController(UserService userService, ResetPasswordService resetPasswordService,
			UserRepository userRepository) {
		this.userService = userService;
		this.resetPasswordService = resetPasswordService;
		//this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(UserDTO userDto) {
		String message = userService.register(userDto);
		return ResponseEntity.ok(message);
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(String email, String password, HttpServletRequest request) {
	    String message = userService.login(email, password);
	    if (message.equals("Successful Login")) {
	        HttpSession session = request.getSession();
	        session.setAttribute("isLoggedIn", true);
	        session.setAttribute("isLoggedInUser", email);
	    }
	    return ResponseEntity.ok(message);
	}

	@GetMapping("/loginStatus")
	public ResponseEntity<Map<String, Boolean>> getLoginStatus(HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    boolean isLoggedIn = session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
	    Map<String, Boolean> response = new HashMap<>();
	    System.out.println("The value of isLoggedIn: "+ isLoggedIn);
	    response.put("isLoggedIn", isLoggedIn);
	    return ResponseEntity.ok(response);
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
	@GetMapping("/userDetails")
	public ResponseEntity<Map<String, String>> getUserDetails(HttpServletRequest request, UserDTO userDTO){
		HttpSession session = request.getSession();
		String userEmail = (String) session.getAttribute("isLoggedInUser");
		User user = userService.fetchUserDetailsByEmail(userEmail);
		String username = user.getFirstName();
		Map<String,String> response = new HashMap<>();
		response.put("isLoggedInUser",username);
		System.out.println("UserName is: "+response);
		return ResponseEntity.ok(response);	
	}
	
}
