package com.solcare.solar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.solcare.solar.entity.Role;
import com.solcare.solar.entity.User;
import com.solcare.solar.repositories.RoleRepository;
import com.solcare.solar.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;	
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	
	public List<User> getUsers(){
		return userRepository.findAll();
	}
	
	public User createUser(User user)	{
		
		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			Optional<Role> userrole = roleRepository.findByName("USER");
			if(userrole.isPresent())
			user.getRoles().add(userrole.get());
			else new IllegalStateException("The 'USER' role does not exist in the database. Please insert it manually.");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
