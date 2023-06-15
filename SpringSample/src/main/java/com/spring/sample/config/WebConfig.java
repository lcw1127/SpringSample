package com.spring.sample.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/*
 * web.xml을 대체하는 클래스
 * WebApplicationInitializer 클래스를 구현하거나
 * AbstractAnnotationConfigDispatcherServletInitializer 클래스를 상속받아 구현한다.
 * 
 * <?xml version="1.0" encoding="UTF-8"?>
	<web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://java.sun.com/xml/ns/javaee https://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
	
		<!-- The definition of the Root Spring Container shared by all Servlets and Filters -->
		<context-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>
				/WEB-INF/spring/root-context.xml
				/WEB-INF/spring/datasource-context.xml
				/WEB-INF/spring/security-context.xml
			</param-value>
		</context-param>
		
		<!-- Spring Security 관련 -->
		<filter>
			<filter-name>springSecurityFilterChain</filter-name>
			<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
		</filter>
		
		<filter-mapping>
			<filter-name>springSecurityFilterChain</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>
		
		<!-- Creates the Spring Container shared by all Servlets and Filters -->
		<listener>
			<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
		</listener>
	
		<!-- Processes application requests -->
		<servlet>
			<servlet-name>appServlet</servlet-name>
			<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
			<init-param>
				<param-name>contextConfigLocation</param-name>
				<param-value>/WEB-INF/spring/appServlet/servlet-context.xml</param-value>
			</init-param>
			<load-on-startup>1</load-on-startup>
		</servlet>
			
		<servlet-mapping>
			<servlet-name>appServlet</servlet-name>
			<url-pattern>/</url-pattern>
		</servlet-mapping>
	
		<!-- 한글 인코딩 -->
		<filter>
			<filter-name>encodingFilter</filter-name>
			<filter-class>
				org.springframework.web.filter.CharacterEncodingFilter
			</filter-class>
			<init-param>
				<param-name>encoding</param-name>
				<param-value>UTF-8</param-value>
			</init-param>
			<init-param>
				<param-name>forceEncoding</param-name>
				<param-value>true</param-value>
			</init-param>
		</filter>
		<filter-mapping>
			<filter-name>encodingFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>
	</web-app>
 */
public class WebConfig implements WebApplicationInitializer {
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		logger.info("onStartup");
		
		// Root Context 관련 등록
		this.registerRootContext(servletContext);

		// DispatcherServlet 등록
		this.registerDispatcherServlet(servletContext);
		
		// 한글 인코딩 관련 필터 등록
		this.registerCharacterEncodingFilter(servletContext);
	}
	
	/*
	 * Root Context 관련 등록
	 * (root-context, datasource-context, security-context..)
	 */
	private void registerRootContext(ServletContext servletContext) {
		logger.info("resterRootContext - ServletContext {}", servletContext);
		
		/*
		<context-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>
				/WEB-INF/spring/root-context.xml
				/WEB-INF/spring/datasource-context.xml
				/WEB-INF/spring/security-context.xml
			</param-value>
		</context-param>
		 */
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(RootContextConfig.class,
								DataSourceContextConfig.class,
								SecurityContextConfig.class);
		
		/*
		<listener>
			<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
		</listener>
		 */
		// Listener에 rootContext 추가
		servletContext.addListener(new ContextLoaderListener(rootContext));
	}
	
	/*
	 * DispatcherServlet 등록
	 */
	private void registerDispatcherServlet(ServletContext servletContext) {
		logger.info("registerDispatcherServlet - ServletContext {}", servletContext);
		
		/*
		<servlet>
			<servlet-name>appServlet</servlet-name>
			<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
			<init-param>
				<param-name>contextConfigLocation</param-name>
				<param-value>/WEB-INF/spring/appServlet/servlet-context.xml</param-value>
			</init-param>
			<load-on-startup>1</load-on-startup>
		</servlet>
		*/
		AnnotationConfigWebApplicationContext servletContextConfig = new AnnotationConfigWebApplicationContext();
		servletContextConfig.register(ServletContextConfig.class);

		ServletRegistration.Dynamic appServlet = servletContext.addServlet("appServlet", new DispatcherServlet(servletContextConfig));
		/*
		<servlet-mapping>	
			<servlet-name>appServlet</servlet-name>
			<url-pattern>/</url-pattern>
		</servlet-mapping>
		*/
		appServlet.setLoadOnStartup(1);
		appServlet.addMapping("/");
	}
	
	/*
	 * 한글 인코딩 관련 필터 등록
	 */
	private void registerCharacterEncodingFilter(ServletContext servletContext) {
		logger.info("registerCharacterEncodingFilter - ServletContext {}", servletContext);
		
		/*
		<!-- 한글 인코딩 -->
		<filter>
			<filter-name>encodingFilter</filter-name>
			<filter-class>
				org.springframework.web.filter.CharacterEncodingFilter
			</filter-class>
			<init-param>
				<param-name>encoding</param-name>
				<param-value>UTF-8</param-value>
			</init-param>
			<init-param>
				<param-name>forceEncoding</param-name>
				<param-value>true</param-value>
			</init-param>
		</filter>
		<filter-mapping>
			<filter-name>encodingFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>
		*/
		FilterRegistration.Dynamic filter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
		
		filter.setInitParameter("encoding", "UTF-8");
		filter.setInitParameter("forceEncoding", "true");
		
		filter.addMappingForUrlPatterns(null, false, "/*");
	}
}

/*
 * WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer 상속 및 구현
 */
/*
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {
				RootConfig.class,
				DataSourceConfig.class
		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {
				ServletConfig.class
		};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {
				"/"
		};
	}

	@Override
	protected Filter[] getServletFilters() {
		/*
		<!-- 한글 인코딩 -->
		<filter>
			<filter-name>encodingFilter</filter-name>
			<filter-class>
				org.springframework.web.filter.CharacterEncodingFilter
			</filter-class>
			<init-param>
				<param-name>encoding</param-name>
				<param-value>UTF-8</param-value>
			</init-param>
			<init-param>
				<param-name>forceEncoding</param-name>
				<param-value>true</param-value>
			</init-param>
		</filter>
		<filter-mapping>
			<filter-name>encodingFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>
		*/
/*
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		
		return new Filter[] {
				encodingFilter
		};
	}
}
*/