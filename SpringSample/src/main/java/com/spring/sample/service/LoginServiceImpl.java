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

// 사용자 정보를 설정할 Custom Bean
/*
 *  현재 security-context.xml에서  security:authentication-provider의 user-service-ref 로 등록되어 있어
 *  Spring Security의 form-login에서 로그인 시 자동으로 수행됨
 *  
 *  여기의 /user/login Rest API 를 통해서는 자동으로 수행되지 않음
 */
@Service
public class LoginServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	/*
	 * @Autowired 대신 Construction Injection 사용 권장
	 *  - 순환 참조 방지
	 *  - 불변성 보장
	 *  - 테스트 용이
	 */
	private final UserDetailsDAOImpl userDetailsDAOImpl;

	@Autowired
	public LoginServiceImpl(UserDetailsDAOImpl userDetailsDAOImpl) {
		this.userDetailsDAOImpl = userDetailsDAOImpl;
	}

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
