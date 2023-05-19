package com.spring.sample.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class SecurityConfig {	
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	
	private final AuthenticationProvider authenticationProvider;
	
	public SecurityConfig(@Qualifier("loginAuthenticationProvider")AuthenticationProvider authenticationProvider) {
		logger.info("SecurityConfig Constructor - loginAuthenticationProvider {}", authenticationProvider);
		
		this.authenticationProvider = authenticationProvider;
	} 
	
	@Bean
	public AuthenticationManager authenticationManager() {
		logger.info("authenticationManager");
		
		List<AuthenticationProvider> authenticationProviderList = new ArrayList<>(); 
		authenticationProviderList.add(authenticationProvider);
		
		ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
		authenticationManager.setAuthenticationEventPublisher(this.defaultAuthenticationEventPublisher());
		
		return authenticationManager;
	}
	
	@Bean
	DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
		return new DefaultAuthenticationEventPublisher();
	}
}
