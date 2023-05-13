package com.spring.sample.DAO;

import java.util.List;

import com.spring.sample.DTO.UserDetailsDTO;

public interface UserDetailsDAO {
	public void printQueryId(String queryId);
	
	// 사용자 조회
	public UserDetailsDTO selectUser(String queryId, String username);
	
	// 사용자 권한 조회
	public List<String> selectUserAuthList(String queryId, String username);
}
