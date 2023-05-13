package com.spring.sample.DAO;

import java.util.List;

import com.spring.sample.DTO.UserInfoDTO;

public interface UserInfoDAO {
	public void printQueryId(String queryId);
	
	// 모든 사용자 조회
	public List<UserInfoDTO> selectAllUserInfo(String queryId) throws Exception;
	
	// 특정 사용자 조회
	public List<UserInfoDTO> selectUserInfoList(String queryId, Object params) throws Exception;
	
	// 쿼리로 특정 사용자 조회
	public Object selectUserInfo(String queryId) throws Exception;
	
	// 조건으로 쿼리로 특정 사용자 조회
	public Object selectUserInfoWithId(String queryId, Object params) throws Exception;
	
	// 사용자 정보 추가
	public Object insertUserInfo(String queryId, Object params) throws Exception;
	
	// 사용자 정보 수정
	public Object updateUserInfo(String queryId, Object params) throws Exception;
	
	// 사용자 정보 삭제
	public Object deleteUserInfo(String queryId, Object params) throws Exception;
}
