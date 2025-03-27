package com.solcare.solar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.solcare.solar.entity.User;
import com.solcare.solar.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//load User From Database
		
		User user = userRepository.findByEmail(username).orElseThrow(()-> new RuntimeException("User Nof found"));
				//.orElseThrow(()-> new RuntimeException("User not Found Exception"));
//			if(user != null) {
//	throw new UsernameNotFoundException("username not found");
//			}
//			
		
		return user;
	}

}
