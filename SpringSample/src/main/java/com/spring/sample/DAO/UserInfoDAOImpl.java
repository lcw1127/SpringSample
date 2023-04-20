package com.spring.sample.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.sample.VO.UserInfoVO;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

@Repository
public class UserInfoDAOImpl implements UserInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserInfoDAOImpl.class);
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public void printQueryId(String queryId) {
		if(logger.isDebugEnabled())
		{
			logger.debug("\t QueryId \t: " + queryId);
		}
	}

	// 모든 사용자 조회
	@Override
	public List<UserInfoVO> selectAllUserInfo(String queryId) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.selectList(queryId);
	}

	// 특정 사용자 조회
	@Override
	public List<UserInfoVO> selectUserInfoList(String queryId, Object params) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.selectList(queryId, params);
	}

	// 쿼리로 특정 사용자 조회
	@Override
	public Object selectUserInfo(String queryId) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.selectOne(queryId);
	}

	// 조건으로 쿼리로 특정 사용자 조회
	@Override
	public Object selectUserInfoWithId(String queryId, Object params) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.selectOne(queryId, params);
	}

	// 사용자 정보 추가
	@Override
	public Object insertUserInfo(String queryId, Object params) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.insert(queryId, params);
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
}
