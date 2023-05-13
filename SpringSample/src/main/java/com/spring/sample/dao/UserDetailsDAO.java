package com.spring.sample.dao;

import java.util.List;

import com.spring.sample.dto.UserDetailsDTO;

public interface UserDetailsDAO {
	public void printQueryId(String queryId);
	
	// 사용자 조회
	public UserDetailsDTO selectUser(String queryId, String username);
	
	// 사용자 권한 조회
	public List<String> selectUserAuthList(String queryId, String username);
}
