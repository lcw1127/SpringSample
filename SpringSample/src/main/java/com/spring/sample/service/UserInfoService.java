package com.spring.sample.service;

import java.util.List;

import com.spring.sample.dto.LoginDTO;
import com.spring.sample.dto.UserInfoDTO;
import com.spring.sample.security.jwt.JwtToken;

public interface UserInfoService {
	// 모든 사용자 조회
	public List<UserInfoDTO> selectUserInfoAll(String queryId) throws Exception;
	
	// 특정 사용자 조회
	public List<UserInfoDTO> selectUserInfoListWithId(String queryId, Object params) throws Exception;
	
	// 쿼리로 특정 사용자 조회
	public Object selectUserInfo(String queryId) throws Exception;
	
	// 조건으로 쿼리로 특정 사용자 조회
	public Object selectUserInfoWithId(String queryId, Object params) throws Exception;
	
	// 사용자 정보 추가
	public Object insertUserInfo(UserInfoDTO userInfoDTO) throws Exception;
	
	// 사용자 정보 추가
	public Object insertUserInfo2(UserInfoDTO userInfoDTO);
	
	// 사용자 정보 수정
	public Object updateUserInfo(String queryId, Object params) throws Exception;
	
	// 사용자 정보 삭제
	public Object deleteUserInfo(String queryId, Object params) throws Exception;
	
	// 사용자 로그인
	public JwtToken login(LoginDTO loginDTO) throws Exception;
}
