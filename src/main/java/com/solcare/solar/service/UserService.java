package com.solcare.solar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.solcare.solar.entity.User;
import com.solcare.solar.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> getUsers(){
		return userRepository.findAll();
	}
	
	public User createUser(User user)
	{
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
