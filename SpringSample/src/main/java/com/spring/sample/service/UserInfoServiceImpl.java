package com.spring.sample.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.spring.sample.dao.UserInfoDAOImpl;
import com.spring.sample.dto.LoginDTO;
import com.spring.sample.dto.UserInfoDTO;
import com.spring.sample.security.jwt.JwtToken;
import com.spring.sample.security.jwt.JwtTokenProvider;

@Service
public class UserInfoServiceImpl implements UserInfoService {
	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	private final UserInfoDAOImpl userInfoDAOImpl;

	private final AuthenticationManager authenticationManager;
	
	private final JwtTokenProvider jwtTokenProvider;
		
	public UserInfoServiceImpl(UserInfoDAOImpl userInfoDAOImpl,
								AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.userInfoDAOImpl = userInfoDAOImpl;
		this.jwtTokenProvider = jwtTokenProvider;
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
	public Object insertUserInfo(String queryId, Object params) throws Exception {
		return userInfoDAOImpl.insertUserInfo(queryId, params);
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
		
		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		JwtToken jwt = jwtTokenProvider.generateToken(authentication);
		
		return jwt;
	}
}
