package com.solcare.solar.controller;

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
import com.solcare.solar.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

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
	
	@ExceptionHandler(BadCredentialsException.class)
	public String excepitonHandler() {
		return "Credentials Invalid !!";
	}
	
	@PostMapping("/create-user")
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}
}
