package com.spring.sample.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.sample.dto.UserInfoDTO;

@Repository
public class UserInfoDAOImpl implements UserInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserInfoDAOImpl.class);
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public UserInfoDAOImpl() {
	}
	
	@Override
	public void printQueryId(String queryId) {
		if(logger.isDebugEnabled())
		{
			logger.debug("\t QueryId \t: " + queryId);
		}
	}

	// 모든 사용자 조회
	@Override
	public List<UserInfoDTO> selectAllUserInfo(String queryId) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.selectList(queryId);
	}

	// 특정 사용자 조회
	@Override
	public List<UserInfoDTO> selectUserInfoList(String queryId, Object params) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.selectList(queryId, params);
	}

	// 쿼리로 특정 사용자 조회
	@Override
	public UserInfoDTO selectUserInfo(String queryId) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.selectOne(queryId);
	}

	// 조건으로 쿼리로 특정 사용자 조회
	@Override
	public UserInfoDTO selectUserInfoWithId(String queryId, Object params) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.selectOne(queryId, params);
	}

	// 사용자 정보 추가
	@Override
	public Object insertUserInfo(String queryId, UserInfoDTO userInfoDTO) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.insert(queryId, userInfoDTO);
	}
	
	// 사용자 권한 정보 추가
	@Override
	public Object insertUserAuthority(String queryId, UserInfoDTO userInfoDTO) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.insert(queryId, userInfoDTO);
	}
	
	// 사용자 등록 히스토리 정보 추가
	@Override
	public Object insertUserHistory(String queryId, UserInfoDTO userInfoDTO) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.insert(queryId, userInfoDTO);
	}
	
	// 사용자 정보 추가
	@Override
	public Object insertTest(UserInfoDTO userInfoDTO)
	{
		String queryId = "";
		int result = 0;
		
		queryId = "user_info_sql.insertUserInfo";
		this.printQueryId(queryId);
		
		result = this.sqlSession.insert(queryId, userInfoDTO);
		logger.info("insertUserInfo {}", result);
		
		queryId = "user_info_sql.insertUserAuthority";
		result = this.sqlSession.insert(queryId, userInfoDTO);
		logger.info("insertUserAuthority {}", result);
		
		queryId = "user_info_sql.insertUserHistory";
		result = this.sqlSession.insert(queryId, userInfoDTO);
		logger.info("insertUserHistory {}", result);
		
		return result;
	}

	// 사용자 정보 수정
	@Override
	public Object updateUserInfo(String queryId, Object params) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.update(queryId, params);
	}

	// 사용자 정보 삭제
	@Override
	public Object deleteUserInfo(String queryId, Object params) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.delete(queryId, params);
	}

	@Override
	public List<String> selectUserAuthorityList(String queryId, String id) {
		this.printQueryId(queryId);
		
		return this.sqlSession.selectList(queryId, id);
	}
}
