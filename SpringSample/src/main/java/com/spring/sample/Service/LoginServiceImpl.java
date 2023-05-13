package com.spring.sample.Service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.sample.DAO.UserDetailsDAOImpl;
import com.spring.sample.DTO.UserDetailsDTO;

@Service("loginService")
public class LoginServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired
	UserDetailsDAOImpl userDetailsDAOImpl;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("loadUserByUsername {}", username);

		ArrayList<String> authList = new ArrayList<String>();

		UserDetailsDTO userDetailsDTO = userDetailsDAOImpl.selectUser("test_user_details.selectUser", username);
		authList = userDetailsDAOImpl.getAuthList("test_user_details.selectAuthList", username);
		
		if (userDetailsDTO == null) { //User을 찾지 못했을 경우
			throw new UsernameNotFoundException(username);
		}
		else {
			userDetailsDTO.setAuthority(authList);
		}
		
		return userDetailsDTO; //완전한 UserDetails 객체
	}

}
