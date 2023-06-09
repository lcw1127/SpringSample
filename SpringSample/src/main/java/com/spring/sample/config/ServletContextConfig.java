package com.spring.sample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/*
 * servlet-context.xml을 대체하는 클래스
 * 
	<?xml version="1.0" encoding="UTF-8"?>
	<beans:beans
		xmlns:mvc="http://www.springframework.org/schema/mvc"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:beans="http://www.springframework.org/schema/beans"
		xmlns:context="http://www.springframework.org/schema/context"
		xmlns:security="http://www.springframework.org/schema/security"
		xsi:schemaLocation="http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security.xsd
			http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd
			http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
			http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
	
		<!-- CORS 문제 해결을 위해 사용 -->
		<mvc:cors>
			<mvc:mapping path="/**"
					allowed-origins="http://localhost:8080"
					allowed-methods="GET,POST,PUT,PATCH,DELETE,OPTIONS"
					allowed-headers="*"
					allow-credentials="true"
					max-age="3600"/>
		</mvc:cors>
		
		<!-- DispatcherServlet Context: defines this servlet's request-processing infrastructure -->
		
		<!-- Enables the Spring MVC @Controller programming model -->
		<mvc:annotation-driven />
	
		<!-- Handles HTTP GET requests for /resources/** by efficiently serving up static resources in the ${webappRoot}/resources directory -->
		<mvc:resources mapping="/resources/**" location="/resources/" />
	
		<!-- Resolves views selected for rendering by @Controllers to .jsp resources in the /WEB-INF/views directory -->
		<beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
			<beans:property name="prefix" value="/WEB-INF/views/" />
			<beans:property name="suffix" value=".jsp" />
		</beans:bean>
	
		<!-- @Secured, @PreAuthorize, @PostAuthorize Annotation 사용을 위해 설정 -->
		<security:global-method-security secured-annotations="enabled" pre-post-annotations="enabled" />
		
		<!--
			root-context.xml에서는 servlet-context.xml 에서 등록된 bean에 접근할 수 없음
			servlet-context.xml에서는 root-context.xml 에서 등록된 bean에 접근할 수 있음
			
			root-cotext.xml에서 Transaction 설정을 해줄 때는 Transaction에 사용되는 Package를 Scan하는 설정을 넣어줘야 함
			-> @Service, @Repository와 같이 Transaction과 관련된 Class들 
			-> @Controller와 같은 Class는 servlet-context.xml에 설정
			
			여기서 com.spring.sample.service에 대해 component-scan을 수행하면 @Transactional로 Transaction 관리가 안됨
		-->
		<context:component-scan base-package="com.spring.sample.controller" />
	</beans:beans>
 */
/*
 * Spring이 제공하는 웹과 관련된 Bean 과 설정 값들을 자동으로 사용할 수 있도록 해줌
 */
@EnableWebMvc
/*
	<context:component-scan base-package="com.spring.sample.controller" />
*/
@ComponentScan(basePackages = {"com.spring.sample.controller"})
public class ServletContextConfig implements WebMvcConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(ServletContextConfig.class);

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		logger.info("addResourceHandlers - ResourceHandlerRegistry {}", registry);
		
		/*
		<mvc:resources mapping="/resources/**" location="/resources/" />
		 */
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		logger.info("configureViewResolvers - ViewResolverRegistry {}", registry);
		
		/*
		<beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
			<beans:property name="prefix" value="/WEB-INF/views/" />
			<beans:property name="suffix" value=".jsp" />
		</beans:bean>
		 */
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		
		bean.setPrefix("/WEB-INF/views");
		bean.setSuffix(".jsp");
		
		registry.viewResolver(bean);
	}
	
}
