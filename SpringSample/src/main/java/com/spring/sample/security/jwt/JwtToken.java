package com.spring.sample.security.jwt;

public class JwtToken {
	private String grantType;
	private String idToken;
	private String accessToken;
	private String refreshToken;
	
	public JwtToken(String grantType, String idToken, String accessToken, String refreshToken) {
		this.grantType = grantType;
		this.idToken = idToken;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getGrantType() {
		return grantType;
	}
	
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getIdToken() {
		return idToken;
	}
	
	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
