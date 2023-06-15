package com.spring.sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/*
 * 별 다른 코드 없이 AbstractSecurityWebApplicationInitializer 를 상속받은 클래스를 만들어 두어야
 * Spring Security Filter 를 등록하고 활성화 시킴
 */
@Configuration
public class WebConfigInitializer extends AbstractSecurityWebApplicationInitializer  {
}
