package com.spring.sample.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.sample.dto.UserInfoDTO;
import com.spring.sample.service.UserInfoServiceImpl;

/**
 * Handles requests for the application home page.
 */
/*
 * 사용시 @Controller와 @ResponseBody가 선언되는것과 같음
 */
@RestController

/*
 * {BASE_URL}/user로 요청오는 것을 처리
 */
@RequestMapping(value = "/user")
public class UserRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	UserInfoServiceImpl userInfoServiceImpl;
	
	/*
	 * /user 으로 요청이 왔을 때 사용됨
	 **/
	@RequestMapping(method = RequestMethod.GET)
	public String home() {
		logger.info("Test /user! - home");
		
		return "home";
	}
	
	/*
	 * /user/json 으로 요청이 왔을 때 사용됨
	 **/
	@RequestMapping(value = "/json", method = RequestMethod.GET)
	public String json() {
		logger.info("Test /user/json!");
		
		UserInfoDTO userInfo = new UserInfoDTO("TEST USER", "TEST PW", "TEST NAME");

		ObjectMapper mapper = new ObjectMapper();
		
		String jsonString = "";
		
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfo);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonString;
	}
	
	/*
	 * /user/db 로 요청이 왔을 때 사용됨
	 * 한글 응답 처리
	 **/
	@RequestMapping(value = "/db", produces = "application/json; charset=utf8", method = RequestMethod.GET)
	public String db() throws Exception {
		logger.info("Test /user/db!");

		List<UserInfoDTO> userInfoList = userInfoServiceImpl.selectUserInfoAll("user_info_sql.selectAll");
		
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonString = "";
		
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfoList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonString;
	}
	
	/*
	 * /user/kor 로 요청이 왔을 때 사용됨
	 * 한글로 된 파라미터 처리
	 **/
	@RequestMapping(value = "/kor", produces = "application/json; charset=utf8", method = RequestMethod.GET)
	public String kor(@RequestParam(defaultValue = "") String name) {
		logger.info("Test /user/kor! Param Name is {}", name);
		
		UserInfoDTO userInfo = new UserInfoDTO("TEST USER", "TEST PW", name);

		ObjectMapper mapper = new ObjectMapper();
		
		String jsonString = "";
		
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfo);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonString;
	}
}
