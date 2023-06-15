package com.spring.sample.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * datasource-context.xml을 대체하는 클래스
 * 
 *	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:beans="http://www.springframework.org/schema/beans"
		xmlns:context="http://www.springframework.org/schema/context"
		xmlns:tx="http://www.springframework.org/schema/tx"
		xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
			http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd
			http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.3.xsd">
		
		<!-- Propertity -->
		<context:property-placeholder location="classpath:config/mysql.properties" /> 
	
		<!-- SQL Session Template -->
		<bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
			<constructor-arg index="0" ref="sqlSession"/>
		</bean>
	
		<!-- SQL Session -->
		<bean id="sqlSession" class="org.mybatis.spring.SqlSessionFactoryBean">
			<property name="dataSource" ref="dataSource" />
			<property name="mapperLocations" value="classpath:/mapper/*_SQL.xml" />
		</bean>
	
		<!-- Data Source -->
		<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
			<property name="driverClassName" value="${mysql.driverClassName}" />
			<property name="url" value="${mysql.url}" />
			<property name="username" value="${mysql.username}" />
			<property name="password" value="${mysql.password}" />
		</bean>
		
		<!-- Transaction Manager -->
		<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
			<beans:property name="dataSource" ref="dataSource" />
		</bean>
		
		<tx:annotation-driven transaction-manager="transactionManager"/>
	</beans>
 */
/*
 * 설정 파일을 대신할 수 있도록 선언
 */
@Configuration

/*
 * <!-- Propertity -->
 * <context:property-placeholder location="classpath:config/mysql.properties" />
 */
@PropertySource("classpath:config/mysql.properties")

/*
 * <tx:annotation-driven transaction-manager="transactionManager"/>
 */
@EnableTransactionManagement
public class DataSourceContextConfig {
	private static final Logger logger = LoggerFactory.getLogger(DataSourceContextConfig.class);
	
	@Value("${mysql.driverClassName}")
	private String driverClassName;
	
	@Value("${mysql.url}")
	private String url;
	
	@Value("${mysql.username}")
	private String username;
	
	@Value("${mysql.password}")
	private String password;
	
	/*
	<!-- SQL Session Template -->
	<bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
		<constructor-arg index="0" ref="sqlSession"/>
	</bean>
	*/
	@Bean
	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
		logger.info("sqlSessionTemplate");
		
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSession().getObject());
		
		return sqlSessionTemplate;
	}
	/*
	<!-- SQL Session -->
	<bean id="sqlSession" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="mapperLocations" value="classpath:/mapper/*_SQL.xml" />
	</bean>
	*/
	@Bean
	public SqlSessionFactoryBean sqlSession() throws IOException {
		logger.info("sqlSession");
		
		SqlSessionFactoryBean sqlSession = new SqlSessionFactoryBean();
		
		sqlSession.setDataSource(dataSource());
		sqlSession.setMapperLocations(new PathMatchingResourcePatternResolver()
											.getResources("classpath:/mapper/*_SQL.xml"));
		
		return sqlSession;
	}
	
	/*
	<!-- Data Source -->
	<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
		<property name="driverClassName" value="${mysql.driverClassName}" />
		<property name="url" value="${mysql.url}" />
		<property name="username" value="${mysql.username}" />
		<property name="password" value="${mysql.password}" />
	</bean>
	*/
	@Bean
	public DataSource dataSource() {
		logger.info("dataSource");
		
		BasicDataSource dataSource = new BasicDataSource();
		
		dataSource.setDriverClassName(this.driverClassName);
		dataSource.setUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		
		return dataSource;
	}
	
	/*
	<!-- Transaction Manager -->
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<beans:property name="dataSource" ref="dataSource" />
	</bean>
	*/
	@Bean
	public DataSourceTransactionManager transactionManager() {
		logger.info("transactionManager");
		
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
		
		return transactionManager;
	}
}
