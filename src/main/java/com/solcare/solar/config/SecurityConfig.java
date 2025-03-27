package com.solcare.solar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.solcare.solar.filter.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
	private JwtAuthenticationEntryPoint point;
	@Autowired
	private JwtAuthenticationFilter filter;
    @Autowired
	private PasswordEncoder passwordEncoder;
    
	@Autowired
	private UserDetailsService userDetailsService;
	

@Bean
@Order(0)
public SecurityFilterChain api(HttpSecurity http) throws Exception {
      http.authorizeHttpRequests(authz -> authz
      .requestMatchers("/admin/**").hasRole("ADMIN")
      .requestMatchers("/user/**").hasRole("USER")
      .requestMatchers("/auth/login", "/auth/**").permitAll()
      .anyRequest().authenticated());
     http.exceptionHandling(authException -> authException
     .authenticationEntryPoint(
        (request, response, ex) -> {
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                ex.getMessage()
            );
        }));
         
     http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
         
        return http.build();
    }  

// @Bean
// @Order(1)
// public SecurityFilterChain app(HttpSecurity http) throws Exception {

//     http.securityMatcher("/app/**")
//     .authorizeHttpRequests(null);

//     return http.build();
// }

@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // Replace with your Angular app's URL
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
	
}