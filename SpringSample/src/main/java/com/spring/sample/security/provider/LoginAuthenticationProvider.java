package com.spring.sample.security.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.spring.sample.dto.UserDetailsDTO;

/*
 *  security-context.xml에서  security:authentication-provider의 ref 로 등록되어 있어
 *  Spring Security의 form-login에서 로그인 시 자동으로 수행됨
 *  
 *  여기의 /user/login Rest API 를 통해서는 자동으로 수행되지 않음
 *  
 *  AuthenticationProvider는 AuthenticationManager에 등록되어야 함
 *  
 *  사용자가 만든 Filter가 Spring Security와 통합하지 않으면
 *  AuthenticationProvider는 사용되지 않을 수 있음
 *  
 */
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(LoginAuthenticationProvider.class);
	
	// DB의 값을 가져다주는 커스터마이징 클래스
	private final UserDetailsService userDetailsServcie;
	
	// 패스워드 암호화 객체
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	LoginAuthenticationProvider(UserDetailsService userDetailsServcie, BCryptPasswordEncoder passwordEncoder) {
		this.userDetailsServcie = userDetailsServcie;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		logger.info("authenticate - authentication {}", authentication);
		
		/* 사용자가 입력한 정보 */
		String userId = authentication.getName();
		String userPw = (String) authentication.getCredentials();
		
		/* DB에서 가져온 정보 */
		UserDetailsDTO userDetailsDTO = (UserDetailsDTO)userDetailsServcie.loadUserByUsername(userId);
		
		/* 인증 진행 */
		// DB에 정보가 없는 경우 예외 발생 (아이디/패스워드 잘못됐을 때와 동일한 것이 좋음)
		// ID 및 PW 체크해서 안맞을 경우 (matches를 이용한 암호화 체크를 해야함)
		if (userDetailsDTO == null || !userId.equals(userDetailsDTO.getUsername())
				|| !this.passwordEncoder.matches(userPw, userDetailsDTO.getPassword())) {
			logger.error("authenticate - BadCredentialsException");
			
			throw new BadCredentialsException(userId);

		// 계정 정보 맞으면 나머지 부가 메소드 체크 (이부분도 필요한 부분만 커스터마이징 하면 됨)
		// 잠긴 계정일 경우
		} else if (!userDetailsDTO.isAccountNonLocked()) {
			logger.error("authenticate - isAccountNonLocked");
			
			throw new LockedException(userId);

		// 비활성화된 계정일 경우
		} else if (!userDetailsDTO.isEnabled()) {
			logger.error("authenticate - isEnabled");
			
			throw new DisabledException(userId);

		// 만료된 계정일 경우
		} else if (!userDetailsDTO.isAccountNonExpired()) {
			logger.error("authenticate - isAccountNonExpired");
			
			throw new AccountExpiredException(userId);

		// 비밀번호가 만료된 경우
		} else if (!userDetailsDTO.isCredentialsNonExpired()) {
			logger.error("authenticate - isCredentialsNonExpired");
			
			throw new CredentialsExpiredException(userId);
		}
		
		/* 최종 리턴 시킬 새로만든 Authentication 객체 */
		Authentication newAuth =
				new UsernamePasswordAuthenticationToken(userDetailsDTO, null, userDetailsDTO.getAuthorities());
		
		logger.info("authenticate - success");
		
		return newAuth;
	}

	@Override
	// 위의 authenticate 메소드에서 반환한 객체가 유효한 타입이 맞는지 검사
	// null 값이거나 잘못된 타입을 반환했을 경우 인증 실패로 간주
	public boolean supports(Class<?> authentication) {
		logger.info("supports - authentication {}", authentication);
		
		// 스프링 Security가 요구하는 UsernamePasswordAuthenticationToken 타입이 맞는지 확인
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
