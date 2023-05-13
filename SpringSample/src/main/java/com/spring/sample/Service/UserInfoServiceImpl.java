package com.spring.sample.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.sample.DAO.UserInfoDAOImpl;
import com.spring.sample.DTO.UserInfoDTO;

@Service
public class UserInfoServiceImpl implements UserInfoService {
	@Autowired
	UserInfoDAOImpl userInfoDAOImpl;
	
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

}
