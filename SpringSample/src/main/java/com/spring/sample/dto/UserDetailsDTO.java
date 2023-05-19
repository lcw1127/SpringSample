package com.spring.sample.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 사용자 로그인시 사용되는 클래스
 */
public class UserDetailsDTO implements UserDetails {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsDTO.class);
	
	private String id;
	private String pw;
	private boolean enabled;
	private ArrayList<GrantedAuthority> authority;
	
	// 권한 관련
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authority;
	}

	public void setAuthority(List<String> authList) {
		logger.info("ID {} AuthList {}", this.id, authList);
		
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		
		for(int i = 0; i < authList.size(); i++) {
			auth.add(new SimpleGrantedAuthority(authList.get(i)));
		}
		
		this.authority = auth;
	}
	
	@Override
	public String getPassword() {
		return this.pw;
	}

	@Override
	public String getUsername() {
		return this.id;
	}

	// 계정이 만료 되었는지?
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠기지 않았는지?
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 패스워드가 만료되지 않았는지?
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 활성화가 되었는지?
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
