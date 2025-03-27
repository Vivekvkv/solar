package com.solcare.solar.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.swing.Spring;

import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtHelper {
	
	//requirement
	public static final long JWT_TOKEN_VALIDITY = 5*60*60;
	
	private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
	
	//retrieve username form jwt
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token,Claims::getSubject);
	}
	
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token,Claims::getExpiration);
		
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	//For Retrieveing any information from toekn we will need to secret key
	
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	//geneerate token for user

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}
	
	//While Createing the Token--
	//1.Define claims of the token liks Isuser, Expiration, Subject and the Id
	//2. Sign the jWT using the HS512 Algorithm and Secret Key
	//3. According to JWS Compact Serialitaion 
	//5. Compaction of the JWT to a uRL--safe string
	
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	//Validate TOken
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return(username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
