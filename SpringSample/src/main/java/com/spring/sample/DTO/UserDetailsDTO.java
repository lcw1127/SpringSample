package com.spring.sample.DTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsDTO implements UserDetails {
	/**
	 * 사용자 로그인시 사용되는 클래스
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsDTO.class);
	
	private String id;
	private String pw;
	private String name;
	private boolean enabled;
	private ArrayList<GrantedAuthority> authority;
	
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

	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
