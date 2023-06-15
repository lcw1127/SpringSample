package com.spring.sample.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.spring.sample.config.SecurityContextConfig;
import com.spring.sample.security.filter.JwtTokenAuthenticationFilter;
import com.spring.sample.security.jwt.JwtTokenProvider;

public class JwtTokenSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
	private static final Logger logger = LoggerFactory.getLogger(SecurityContextConfig.class);
	
	private final JwtTokenProvider jwtTokenProvider;
	
	public JwtTokenSecurityConfig(JwtTokenProvider jwtTokenProvider) {
		logger.info("JwtTokenSecurityConfig - jwtTokenProvider {}", jwtTokenProvider);
		
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void configure(HttpSecurity builder) throws Exception {
		logger.info("configure - HttpSecurity {}", builder);
		
		/*
		 * <!--
			Filter 설정
			다음은 Filter 클래스가 동작하는 순서대로 나열한 내용으로 이 외에도 더 많은 종류의 필더가 있으며 아래 사이트에서 확인 할 수 있음
			 - https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-security-filters
			
			1. SecurityContextPersistenceFilter : SecurityContextRepository에서 SecurityContext를 가져오거나 생성
			2. LogoutFilter : Logout 요청을 처리하며 로그아웃 요청 시에만 처리
			3. UsernamePasswordAuthenticationFilter : ID와 Password를 사용하는 실제 Form 기반 유저 인증을 처리
			4. ConcurrentSessionFilter : 동시 세션과 관련된 필터, 현재 사용자 계정으로 인증을 받은 사용자가 두 명 이상일 때 실행되는 필터
			5. RememberMeAuthenticationFilter : 세션이 사라지거나 만료 되더라도, 쿠키 또는 DB를 사용하여 저장된 토큰 기반으로 인증을 처리
			6. AnonymousAuthenticationFilter : 사용자 정보가 인증되지 않았다면 익명 사용자 토큰을 반환
			7. SessionManagementFilter : 로그인 후 Session과 관련된 작업을 처리
			
			아래 2개는 인증 이후 자원에 접근할 때 사용되는 필터
			8. ExceptionTranslationFilter : 필터 체인 내에서 발생되는 인증, 인가 예외를 처리
			9. FilterSecurityInterceptor : 권한 부여와 관련한 결정을 AccessDecisionManager에게 위임해 권한부여 결정 및 접근 제어 처리
			
			아래는 custom-filter를 설정하는 것으로 여기에서 사용할 수 있는 필터의 종류는 다음과 같다.
			Alias						Filter Class							Namespace Element or Attribute
			CHANNEL_FILTER				ChannelProcessingFilter					http/intercept-url@requires-channel
			SECURITY_CONTEXT_FILTER		SecurityContextPersistenceFilter		http
			CONCURRENT_SESSION_FILTER	ConcurrentSessionFilter					session-management/concurrency-control
			LOGOUT_FILTER				LogoutFilter							http/logout
			X509_FILTER					X509AuthenticationFilter				http/x509
			PRE_AUTH_FILTER				AstractPreAuthenticatedProcessingFilter Subclasses	N/A
			CAS_FILTER					CasAuthenticationFilter					N/A
			FORM_LOGIN_FILTER			UsernamePasswordAuthenticationFilter	http/form-login
			BASIC_AUTH_FILTER			BasicAuthenticationFilter				http/http-basic
			SERVLET_API_SUPPORT_FILTER	SecurityContextHolderAwareRequestFilter	http/@servlet-api-provision
			JAAS_API_SUPPORT_FILTER		JaasApiIntegrationFilter				http/@jaas-api-provision
			REMEMBER_ME_FILTER			RememberMeAuthenticationFilter			http/remember-me
			ANONYMOUS_FILTER			AnonymousAuthenticationFilter			http/anonymous
			SESSION_MANAGEMENT_FILTER	SessionManagementFilter					session-management
			EXCEPTION_TRANSLATION_FILTER	ExceptionTranslationFilter			http
			FILTER_SECURITY_INTERCEPTOR	FilterSecurityInterceptor				http
			SWITCH_USER_FILTER			SwitchUserFilter						N/A
			
			아래는 FORM_LOGIN_FILTER(UsernamePasswordAuthenticationFilter) 전에
			사용자가 만든 jwtTokenAuthenticationFilter 를 사용하며 여기에서 통과가 되면
			FORM_LOGIN_FILTER 이후의 필터는 통과한 것으로 본다는 뜻으로
			Username + Password의 인증을 JWT를 통해 수행하겠다는 것
			-->
			<security:custom-filter before="FORM_LOGIN_FILTER" ref="jwtTokenAuthenticationFilter" />
		 */
		JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter = new JwtTokenAuthenticationFilter(this.jwtTokenProvider);
		
		builder.addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
