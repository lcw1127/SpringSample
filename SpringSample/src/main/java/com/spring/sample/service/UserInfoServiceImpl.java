package com.spring.sample.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.spring.sample.dao.UserInfoDAOImpl;
import com.spring.sample.dto.LoginDTO;
import com.spring.sample.dto.UserInfoDTO;
import com.spring.sample.security.jwt.JwtToken;
import com.spring.sample.security.jwt.JwtTokenProvider;

@Service
public class UserInfoServiceImpl implements UserInfoService {
	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	private final UserInfoDAOImpl userInfoDAOImpl;

	//private final AuthenticationManager authenticationManager;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	/*
	 * Transaction을 수동으로 관리하기 위해 사용
	 */
	private final DataSourceTransactionManager transactionManager;
		
	public UserInfoServiceImpl(UserInfoDAOImpl userInfoDAOImpl,
								//AuthenticationManager authenticationManager,
			 					AuthenticationManagerBuilder authenticationManagerBuilder,
								JwtTokenProvider jwtTokenProvider,
								DataSourceTransactionManager transactionManager) {
		//this.authenticationManager = authenticationManager;
		this.userInfoDAOImpl = userInfoDAOImpl;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.transactionManager = transactionManager;
	}
	
	@Override
	public List<UserInfoDTO> selectUserInfoAll(String queryId) throws Exception {
		return userInfoDAOImpl.selectAllUserInfo(queryId);
	}

	@Override
	public List<UserInfoDTO> selectUserInfoListWithId(String queryId, Object params) throws Exception {
		return userInfoDAOImpl.selectUserInfoList(queryId, params);
	}

	@Override
	public Object selectUserInfo(String queryId) throws Exception {
		return userInfoDAOImpl.selectUserInfo(queryId);
	}

	@Override
	public Object selectUserInfoWithId(String queryId, Object params) throws Exception {
		return userInfoDAOImpl.selectUserInfoWithId(queryId, params);
	}

	@Override
	public Object insertUserInfo(UserInfoDTO userInfoDTO) throws Exception {
		int result = 0;
		
		/*
		 * Transaction 발생 전 getTransactiond을 사용해 TransactionStatus를 보관
		 */
		TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			result = (int)userInfoDAOImpl.insertUserInfo("user_info_sql.insertUserInfo", userInfoDTO);
			logger.info("insertUserInfo {}", result);
			
			result = (int)userInfoDAOImpl.insertUserAuthority("user_info_sql.insertUserAuthority", userInfoDTO);
			logger.info("insertUserAuthority {}", result);
			
			result = (int)userInfoDAOImpl.insertUserHistory("user_info_sql.insertUserHistory", userInfoDTO);
			logger.info("insertUserHistory {}", result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			/*
			 * 에러 발생 시 보관중인 TransactionStatus를 사용해 Rollback 수행
			 */
			transactionManager.rollback(txStatus);
			
			throw new Exception("Insert User Info Failure");
		}
		
		/*
		 * 정상으로 처리된 경우 보관중인 TransactionStatus를 사용해 Commit 수행
		 */
		transactionManager.commit(txStatus);
		
		return result;
	}

	@Override
	public Object insertUserInfo2(UserInfoDTO userInfoDTO) {
		int result = 0;
		
		result = (int)userInfoDAOImpl.insertTest(userInfoDTO);
		logger.info("insertTest {}", result);
		
		return result;
	}
	
	@Override
	public Object updateUserInfo(String queryId, Object params) throws Exception {
		return userInfoDAOImpl.updateUserInfo(queryId, params);
	}

	@Override
	public Object deleteUserInfo(String queryId, Object params) throws Exception {
		return userInfoDAOImpl.deleteUserInfo(queryId, params);
	}

	public JwtToken login(LoginDTO loginDTO) {
		logger.info("login Data {}", loginDTO);
		
		/*
		 * 하단의 Authentication과 관련된 부분을 사용하지 않고
		 * 직접 DB에서 사용자 정보를 조회해 비밀번호를 비교해도 되며
		 *  generateToken으로 사용자의 Autority 만 전달해서 사용해도 됨
		 */
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginDTO.getId(), loginDTO.getPw());
		
		//Authentication authentication = authenticationManager.authenticate(authenticationToken);
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		JwtToken jwt = jwtTokenProvider.generateToken(authentication);
		
		return jwt;
	}
}
