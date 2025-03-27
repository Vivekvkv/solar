package com.solcare.solar.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solcare.solar.entity.User;
import com.solcare.solar.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("home")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@CrossOrigin(origins = "http://localhost:4200")
public class HomeController {
		Logger logger = LoggerFactory.getLogger(HomeController.class);
	
		
		@Autowired
		private UserService userService;
		
		
		@RequestMapping(value="users" , method = {RequestMethod.GET})
		public List<User> test(@RequestHeader(name = "Authorization") String authorizationHeader ,HttpServletRequest httpServletRequest) {
			this.logger.warn("this is Working Message");
			return this.userService.getUsers();
		}
		
		@PreAuthorize("hasRole('ADMIN')")
		@RequestMapping(value = "admin", method = RequestMethod.GET)
		public String admin() {
			this.logger.warn("this is Working Message");
			return "Admi Acess";
		}
			
			@GetMapping("/Radmin")
			@Secured("USER")
			public List<User> rAdmin() {
				this.logger.warn("this is Working Message");
				return this.userService.getUsers();
			}
			
			@PreAuthorize("hasRole('ROLE_USER')")
			@RequestMapping("/Ruser")
			public String ruser() {
				this.logger.warn("this is Working Message");
				return "Role USER";
		}
}
