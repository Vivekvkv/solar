package com.solcare.solar.service;

import java.util.Optional;

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
		
		if (isValidPhoneNumber(username)) {
            // Load user by phone number if it is a valid phone number
            return loadUserByMobileNO(Long.parseLong(username));  // Assuming mobile number is stored as Long
        } else {
            // Load user by email if it is not a mobile number
            return loadUserByEmail(username);
        }
	}

	public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email: " + email) );
        return user;
	    }

    // Load user by mobile number
    public UserDetails loadUserByMobileNO(Long mobileNumber) {
         User user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(()->new UsernameNotFoundException("User not found with email: " + mobileNumber) );
        return user; }

	private boolean isValidPhoneNumber(String str) {
        // Check if the string contains only digits and is the correct length
        return str.matches("[0-9]{10}");  // A simple check for a 10-digit phone number
    }
}
