package com.spring.sample.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.sample.dao.UserDetailsDAOImpl;
import com.spring.sample.dto.UserDetailsDTO;

@Service("loginService")
public class LoginServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired
	UserDetailsDAOImpl userDetailsDAOImpl;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("loadUserByUsername {}", username);

		List<String> authList = null;

		UserDetailsDTO userDetailsDTO = userDetailsDAOImpl.selectUser("user_details_sql.selectUser", username);
		authList = userDetailsDAOImpl.selectUserAuthList("user_details_sql.selectAuthList", username);
		
		if (userDetailsDTO == null) { //User을 찾지 못했을 경우
			throw new UsernameNotFoundException(username);
		}
		else {
			userDetailsDTO.setAuthority(authList);
		}
		
		return userDetailsDTO; //완전한 UserDetails 객체
	}
}
