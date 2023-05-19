package com.spring.sample.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.sample.security.jwt.JwtTokenProvider;

/*
 *  UsernamePasswordAuthenticationFilter 이전에 실행에 하도록 security-context.xml에 등록
 *  OncePerRequestFilter 를 extends 하는 것도 고려
 *  
 *  Spring Security의 Filter와 통합하지 않았기 때문에
 *  SecurityContextHolder를 직접 설정할 수 있어 AuthenticationManager를 사용하지 않아도 된다.
 *  (If you are not integrating with Spring Security’s Filters instances
 *  you can set the SecurityContextHolder directly and are not required to use an AuthenticationManager.)
 *  
 *  extends GenericFilterBean -> extends OncePerRequestFilter 로 변경
 *  요청이 오는 경우 한 번만 호출되게 하기 위해 변경
 */
@Service
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);
	
    public static final String AUTHORIZATION_HEADER = "Authorization";
    
    public static final String AUTHORIZATION_TOKEN = "access_token";
    
    public static final String TYPE_BEARER = "Bearer";
    public static final String TYPE_REFRESH = "Refresh";
    
    public static final int EXPIRED_TIME = 60 * 60 * 24; 
    
    private final JwtTokenProvider jwtTokenProvider;
    
    public JwtTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    	this.jwtTokenProvider = jwtTokenProvider;
    }
    
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("doFilter");
		
		// 1. JWT 토큰 추출
        String jwt = resolveToken(request);
        logger.info("doFilter - jwt {}", jwt);
        
        if (StringUtils.hasText(jwt) && this.jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = this.jwtTokenProvider.getAuthentication(jwt);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        logger.info("doFilter - filterChain.doFilter");
        
        filterChain.doFilter(request, response);
	}
	
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TYPE_BEARER)) {
			return bearerToken.substring(7);
		}
		
		String jwt = request.getParameter(AUTHORIZATION_TOKEN);
		
		if (StringUtils.hasText(jwt)) {
			return jwt;
		}
		
		return null;
	}
}
