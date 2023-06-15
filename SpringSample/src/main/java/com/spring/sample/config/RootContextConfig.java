package com.spring.sample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/*
 * root-context.xml을 대체하는 클래스
 * 
 *	<?xml version="1.0" encoding="UTF-8"?>
 	<beans xmlns="http://www.springframework.org/schema/beans"
 		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 		xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd">
 		
 		<!-- Root Context: defines shared resources visible to all other web components -->
 			
 	</beans> 
 */
/*
 * 설정 파일을 대신할 수 있도록 선언
 */
@Configuration

/*
 * datasource-context.xml에 넣었던 부분은 root-context.xml 쪽으로 이동
 * <context:component-scan base-package="com.spring.sample.dao" />
 * <context:component-scan base-package="com.spring.sample.service" />
 */
@ComponentScan(basePackages = {
					"com.spring.sample.dao",
					"com.spring.sample.service"
				})
public class RootContextConfig {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(RootContextConfig.class);
}
