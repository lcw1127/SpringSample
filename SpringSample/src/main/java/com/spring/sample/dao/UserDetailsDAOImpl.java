package com.spring.sample.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.sample.dto.UserDetailsDTO;

@Repository
public class UserDetailsDAOImpl implements UserDetailsDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsDAOImpl.class);
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public void printQueryId(String queryId) {
		if(logger.isDebugEnabled())
		{
			logger.debug("\t QueryId \t: " + queryId);
		}
	}

	@Override
	public UserDetailsDTO selectUser(String queryId, String username) {
		this.printQueryId(queryId);
		
		return this.sqlSession.selectOne(queryId, username);
	}

	@Override
	public List<String> selectUserAuthList(String queryId, String username) {
		this.printQueryId(queryId);
		
		return this.sqlSession.selectList(queryId, username);
	}

}
