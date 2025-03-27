package com.solcare.solar.entity;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest {

	private String email;
	private String password;
}
