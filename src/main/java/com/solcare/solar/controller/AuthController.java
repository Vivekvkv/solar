package com.solcare.solar.controller;

import java.lang.foreign.Linker.Option;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.solcare.solar.entity.JwtRequest;
import com.solcare.solar.entity.JwtResponse;
import com.solcare.solar.entity.User;
import com.solcare.solar.helper.JwtHelper;
import com.solcare.solar.repositories.UserRepository;
import com.solcare.solar.service.OtpService;
import com.solcare.solar.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("auth")
public class AuthController {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OtpService otpService;
	
	
	private Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	
	
	@RequestMapping(value  = "login", method = {RequestMethod.POST})
	public ResponseEntity<JwtResponse>login(@RequestBody JwtRequest request){
		
			this.doAuthenticate(request.getEmail(), request.getPassword());
	
			UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
			String token = this.jwtHelper.generateToken(userDetails);
		
			JwtResponse response = JwtResponse.builder()
					.jwtToken(token)
					.username(userDetails.getUsername()).build();
			return new ResponseEntity<JwtResponse>(response, HttpStatus.OK);
	}
	
	private void doAuthenticate(String email, String Password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, Password);
		try {
			manager.authenticate(authenticationToken);
		}catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Username or Password !!!");
		}
	}

	 // Endpoint 1: Send OTP to the user (via email or phone number)
	 @PostMapping("send-otp")
	 public ResponseEntity<String> sendOTP(@RequestBody Map<String, String> data) {
		 // Check if user exists, if not, create a new entry
		 String mobileNumber = data.get("mobileNumber");
		 Optional<User> OpUser = userRepository.findByMobileNumber(Long.parseLong(mobileNumber));
		
		 if (!OpUser.isPresent()) {
			//User user = OpUser.get();
			 // Create new user if not found
			 User user = new User();
			 user.setMobileNumber(Long.parseLong(mobileNumber));
			 user.setOtpRequestCount(0l);
			 user.setOtpRequestTime(System.currentTimeMillis());
			 userRepository.save(user);
		 }
 
		 // Send OTP to the user's phone or email
		 String response = otpService.sendOTP(Long.parseLong(mobileNumber));
		 
		 if (response.equals("OTP sent successfully!")) {
			 return ResponseEntity.ok("OTP sent successfully!");
		 } else {
			 return ResponseEntity.status(400).body(response);
		 }
	 }

	 // Endpoint 2: Verify OTP and log the user in (return JWT)
	 @PostMapping("/verify-otp")
	 public ResponseEntity<String> verifyOTP(@RequestParam Long phoneNumberOrEmail, @RequestParam String otp) {
		 // Check if user exists
		 Optional<User> user = userRepository.findByMobileNumber(phoneNumberOrEmail);
		
		 if (user == null) {
			 return ResponseEntity.status(400).body("User not found!");
		 }
		 
		 // Validate the OTP
		 boolean isOtpValid = otpService.validateOTP(otp, phoneNumberOrEmail);
 
		 if (isOtpValid) {
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(user.get().getMobileNumber().toString());
   
			 // OTP is valid, generate JWT token
			 String token = jwtHelper.generateToken(userDetails);
			 return ResponseEntity.ok("OTP verified successfully. JWT Token: " + token);
		 } else {
			 return ResponseEntity.status(400).body("Invalid OTP.");
		 }
	 }
 



	
	@ExceptionHandler(BadCredentialsException.class)
	public String excepitonHandler() {
		return "Credentials Invalid !!";
	}
	
	@PostMapping("create-user")
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}

	@PostMapping("hello")
	public String getMethodName(@RequestBody User user) {
		return new String(user.getEmail());
	}
	
}
