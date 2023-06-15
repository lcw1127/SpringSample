package com.spring.sample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.sample.dao.UserDetailsDAOImpl;
import com.spring.sample.security.config.JwtTokenSecurityConfig;
import com.spring.sample.security.filter.JwtTokenAuthenticationFilter;
import com.spring.sample.security.jwt.JwtTokenProvider;
import com.spring.sample.security.provider.LoginAuthenticationProvider;
import com.spring.sample.service.LoginServiceImpl;
/*
 * security-context.xml을 대체하는 클래스
 * 
 *	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:beans="http://www.springframework.org/schema/beans"
		xmlns:security="http://www.springframework.org/schema/security"
		xmlns:context="http://www.springframework.org/schema/context"
		xsi:schemaLocation="http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security-5.2.xsd
			http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
			http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
		
		<!-- Propertity -->
		<!-- <context:property-placeholder location="classpath:config/jwt.properties" /> --> 
		
		<!-- <security:http pattern="/resources/**" security="none" /> -->
		
		<!-- @Secured, @PreAuthorize, @PostAuthorize Annotation 사용을 위해 설정 -->
		<!--
		<security:global-method-security secured-annotations="enabled" pre-post-annotations="enabled" />
		-->
	
		<security:http auto-config="true" use-expressions="true">
			<!-- csrf Disabled -->
			<security:csrf disabled="true"/>
	
			<!--
				* 위에서 부터 순서대로 적용 되므로 허용할 범위를 먼저 적용 후 금지할 범위를 적용하도록 한다.
				
				hasRole('role')				해당 권한이 있으면 요청한 페이지를, 없으면 로그인 페이지로 이동
				hasAnyRole('role1,'role2')	포함된 권한 중 하나라도 있으면 요청한 페이지를, 없으면 로그인 페이지로 이동
				isAuthenticated()		로그인 인증을 받은 사용자는 권한에 관계 없이 허용, 익명 사용자는 로그인 페이지로 이동
				isFullyAuthenticated()	자동 로그인하지 않고 로그인 인증을 한 사용자는 권한에 관계 없이 허용
				isAnonymous()	권한이 없는 익명의 사용자만 접근을 허용함 (로그인되어 권한이 있으면 접근 불가)
				isRememberMe()	자동 로그인 대상 사용자의 경우 접근을 허용
				permitAll	모두 접근 허용
				denyAll		모두 접근 불가
			-->
			<!-- 정적 리소스는 모두 접근 허용 -->
			<security:intercept-url pattern="/resources/**" access="permitAll" />
			
			<!-- URI 별로 설정 -->
			<security:intercept-url pattern="/" access="permitAll"/>
			
			<security:intercept-url pattern="/user" access="permitAll"/>
			
			<security:intercept-url pattern="/user/login" access="isAnonymous()"/>
			<security:intercept-url pattern="/user/join" access="hasRole('ADMIN')"/>
			
			<security:intercept-url pattern="/user/json" access="hasRole('USER')"/>
			<security:intercept-url pattern="/user/db" access="isAuthenticated()"/>
			
			<!-- Logout -->
			<security:logout logout-url="/user/logout" logout-success-url="/" delete-cookies="true" invalidate-session="true" />
			
			<!--
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
		</security:http>
		
		<security:authentication-manager>
			<!-- 권한을 설정할 Custom Provider Bean 설정, Provider를 등록하여 사용자 로그인 처리 -->
			<security:authentication-provider ref="loginAuthenticationProvider" />
			
			<!-- 사용자 정보를 설정할 Custom Service Bean 설정 -->
			<security:authentication-provider user-service-ref="loginServiceImpl">
				<security:password-encoder ref="passwordEncoder" />
			</security:authentication-provider>
		</security:authentication-manager>
		
		<!-- 패스워드 단방향 암호화 -->
		<bean id="passwordEncoder"
			class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"/>
			
		<!-- Component Scan -->
		<context:component-scan base-package="com.spring.sample.security" />
	</beans>
 */
/*
 * 내부적으로
 * 		WebSecurityConfiguration.class,
 * 		SpringWebMvcImportSelector.class,
 * 		OAuth2ImportSelector.class 들을 Import 하여 웹 보안 활성화
 * 
 * 내부적으로 @Configuration 을 사용해 설정파일을 대신할 수 있도록 함
 */
@EnableWebSecurity

/*
 * Method에 부가적으로 설정할 수 있는 Annotation을 사용할 수 있도록 함
 *  - prePostEnabled, securedEnabled, jsr250Enabled 가 있음
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)

/*
 * <context:component-scan base-package="com.spring.sample.security" />
 */
@ComponentScan(basePackages = {
					"com.spring.sample.security"
				})
public class SecurityContextConfig extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(SecurityContextConfig.class);
	
	private final JwtTokenProvider jwtTokenProvider;
//	private final AuthenticationProvider authenticationProvider;
//	private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;
//	
//	public SecurityContextConfig(@Qualifier("loginAuthenticationProvider")AuthenticationProvider authenticationProvider,
//									JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter) {
//		logger.info("SecurityConfig Constructor - loginAuthenticationProvider {}", authenticationProvider);
//		
//		this.authenticationProvider = authenticationProvider;
//		this.jwtTokenAuthenticationFilter = jwtTokenAuthenticationFilter;
//	} 
	public SecurityContextConfig(JwtTokenProvider jwtTokenProvider) {
		logger.info("SecurityContextConfig - JwtTokenProvider {}", jwtTokenProvider);
		
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info("configure - HttpSecurity {}", http);
		/*
			<security:http auto-config="true" use-expressions="true">
				<!--
					* 위에서 부터 순서대로 적용 되므로 허용할 범위를 먼저 적용 후 금지할 범위를 적용하도록 한다.
					
					hasRole('role')				해당 권한이 있으면 요청한 페이지를, 없으면 로그인 페이지로 이동
					hasAnyRole('role1,'role2')	포함된 권한 중 하나라도 있으면 요청한 페이지를, 없으면 로그인 페이지로 이동
					isAuthenticated()		로그인 인증을 받은 사용자는 권한에 관계 없이 허용, 익명 사용자는 로그인 페이지로 이동
					isFullyAuthenticated()	자동 로그인하지 않고 로그인 인증을 한 사용자는 권한에 관계 없이 허용
					isAnonymous()	권한이 없는 익명의 사용자만 접근을 허용함 (로그인되어 권한이 있으면 접근 불가)
					isRememberMe()	자동 로그인 대상 사용자의 경우 접근을 허용
					permitAll	모두 접근 허용
					denyAll		모두 접근 불가
				-->
				<!-- 정적 리소스는 모두 접근 허용 -->
				<security:intercept-url pattern="/resources/**" access="permitAll" />
				
				<!-- URI 별로 설정 -->
				<security:intercept-url pattern="/" access="permitAll"/>
				
				<security:intercept-url pattern="/user" access="permitAll"/>
				
				<security:intercept-url pattern="/user/login" access="isAnonymous()"/>
				<security:intercept-url pattern="/user/join" access="hasRole('ADMIN')"/>
				
				<security:intercept-url pattern="/user/json" access="hasRole('USER')"/>
				<security:intercept-url pattern="/user/db" access="isAuthenticated()"/>
				
				<!-- Logout -->
				<security:logout logout-url="/user/logout" logout-success-url="/" delete-cookies="true" invalidate-session="true" />
			</security:http>
		 */
		http
			/*
			 * <!-- csrf Disabled -->
			 * <security:csrf disabled="true"/>
			 */ 
			.csrf()
				.disable()

			/*
			 * Form Login Disable
			 * XML 에서는 없음
			 */
			.formLogin()
				.disable()

			/*
			 * Http Basic Auth(ID / PW) Disable
			 * XML 에서는 없음
			 */
			.httpBasic()
				.disable()

			/*
			 * SessionCreationPolicy : Spring의 Security 세션 정책
			 * ALWAYS : 항상 Session을 생성
			 * IF_REQUIRED : 필요시 Session을 생성(Default)
			 * NEVER : Session을 생성하지 않지만 기존에 존재하면 사용
			 * STATELESS : Session을 생성하지 않고 기존것도 사용하지 않도록 함
			 */
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			
			.and()
				.authorizeRequests()
					/*
					 *	<!--
						위에서 부터 순서대로 적용 되므로 허용할 범위를 먼저 적용 후 금지할 범위를 적용하도록 한다.
						
						hasRole('role')				해당 권한이 있으면 요청한 페이지를, 없으면 로그인 페이지로 이동
						hasAnyRole('role1,'role2')	포함된 권한 중 하나라도 있으면 요청한 페이지를, 없으면 로그인 페이지로 이동
						isAuthenticated()		로그인 인증을 받은 사용자는 권한에 관계 없이 허용, 익명 사용자는 로그인 페이지로 이동
						isFullyAuthenticated()	자동 로그인하지 않고 로그인 인증을 한 사용자는 권한에 관계 없이 허용
						isAnonymous()	권한이 없는 익명의 사용자만 접근을 허용함 (로그인되어 권한이 있으면 접근 불가)
						isRememberMe()	자동 로그인 대상 사용자의 경우 접근을 허용
						permitAll	모두 접근 허용
						denyAll		모두 접근 불가
						-->
					 	<!-- 정적 리소스는 모두 접근 허용 -->
						<security:intercept-url pattern="/resources/**" access="permitAll" />
					 */
					.antMatchers("/resources/**").permitAll()
					
					/*
						<!-- URI 별로 설정 -->
						<security:intercept-url pattern="/" access="permitAll"/> 
					 */
					.antMatchers("/").permitAll()
					
					/*
					 * <security:intercept-url pattern="/user" access="permitAll"/>
					 */
					.antMatchers("/user").permitAll()
					
					/*
					 * <security:intercept-url pattern="/user/login" access="isAnonymous()"/>
					 */
					.antMatchers("/user/login").anonymous()
					
					/*
					 * <security:intercept-url pattern="/user/join" access="hasRole('ADMIN')"/>
					 */
					.antMatchers("/user/join").hasRole("ADMIN")
					
					/*
					 * <security:intercept-url pattern="/user/json" access="hasRole('USER')"/>
					 */
					.antMatchers("/user/json").hasRole("USER")
					
					/*
					 * <security:intercept-url pattern="/user/db" access="isAuthenticated()"/>
					 */
					.antMatchers("/user/db").authenticated()
			.and()
				.apply(jwtTokenSecurityConfig());
	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		logger.info("configure - AuthenticationManagerBuilder {}", auth);
//		/*
//		 *	<security:authentication-manager>
//				<!-- 권한을 설정할 Custom Provider Bean 설정, Provider를 등록하여 사용자 로그인 처리 -->
//				<security:authentication-provider ref="loginAuthenticationProvider" />
//				
//				<!-- 사용자 정보를 설정할 Custom Service Bean 설정 -->
//				<security:authentication-provider user-service-ref="loginServiceImpl">
//					<security:password-encoder ref="passwordEncoder" />
//				</security:authentication-provider>
//			</security:authentication-manager>
//		 */
//		auth.authenticationProvider(loginAuthenticationProvider());
//	}
	
	private JwtTokenSecurityConfig jwtTokenSecurityConfig() {
		logger.info("jwtTokenSecurityConfig");
		
		return new JwtTokenSecurityConfig(jwtTokenProvider);
	}
	
//	@Bean
//	public AuthenticationManager authenticationManager() {
//		logger.info("authenticationManager");
//		
//		/*
//		 *	<security:authentication-manager>
//				<!-- 권한을 설정할 Custom Provider Bean 설정, Provider를 등록하여 사용자 로그인 처리 -->
//				<security:authentication-provider ref="loginAuthenticationProvider" />
//			</security:authentication-manager>
//		 */
//		List<AuthenticationProvider> authenticationProviderList = new ArrayList<>(); 
//		authenticationProviderList.add(authenticationProvider);
//		
//		ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
//		authenticationManager.setAuthenticationEventPublisher(this.defaultAuthenticationEventPublisher());
//		
//		return authenticationManager;
//	}
	
//	@Bean
//	DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
//		return new DefaultAuthenticationEventPublisher();
//	}
	
	/*
	<!-- 패스워드 단방향 암호화 -->
	<bean id="passwordEncoder"
		class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"/>
	*/
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public LoginServiceImpl loginServiceImpl() {
		return new LoginServiceImpl(userDetailsDAOImpl());
	}
	
	@Bean
	public UserDetailsDAOImpl userDetailsDAOImpl() {
		logger.info("userDetailsDAOImpl");
		
		return new UserDetailsDAOImpl();
	};
	
	@Bean
	public LoginAuthenticationProvider loginAuthenticationProvider() {
		logger.info("loginAuthenticationProvider");
		
		return new LoginAuthenticationProvider(loginServiceImpl(), (BCryptPasswordEncoder)passwordEncoder());
	}

	@Bean
	public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() {
		logger.info("jwtTokenAuthenticationFilter");
		
		return new JwtTokenAuthenticationFilter(this.jwtTokenProvider);
	}
}
