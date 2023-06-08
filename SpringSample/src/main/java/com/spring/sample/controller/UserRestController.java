package com.spring.sample.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.sample.dto.LoginDTO;
import com.spring.sample.dto.UserInfoDTO;
import com.spring.sample.security.filter.JwtTokenAuthenticationFilter;
import com.spring.sample.security.jwt.JwtToken;
import com.spring.sample.service.UserInfoServiceImpl;

/**
 * Handles requests for the application home page.
 */
/*
 * 사용시 @Controller와 @ResponseBody가 선언되는것과 같음
 */
@RestController

/*
 * {BASE_URL}/user 로 요청오는 것을 처리
 */
@RequestMapping(value = "/user")
public class UserRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	//@Autowired
	private final UserInfoServiceImpl userInfoServiceImpl;
	
	// @Autowired 대신 Construction Injection 사용
	public UserRestController(UserInfoServiceImpl userInfoServiceImpl) {
		this.userInfoServiceImpl = userInfoServiceImpl;
	}
	
	/*
	 * /user 로 요청이 왔을 때 사용됨
	 **/
	@RequestMapping(method = RequestMethod.GET)
	public String home() {
		logger.info("Test /user/rest! - home");
		
		return "home";
	}

	/*
	 * /user/login 으로 요청이 왔을 때 사용됨
	 **/
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
		logger.info("Test POST /user/login, Data {}", loginDTO.toString());

		JwtToken jwt = null;
		
		try {
			jwt = userInfoServiceImpl.login(loginDTO);
		} catch (Exception e) {
			return new ResponseEntity<>("{\"errMsg\" : \"id or password is incorrect.\"}", null, HttpStatus.BAD_REQUEST);
		}

		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.add(JwtTokenAuthenticationFilter.AUTHORIZATION_HEADER,
						JwtTokenAuthenticationFilter.TYPE_BEARER + " " + jwt.getAccessToken());
		
		logger.info("Headers {}", httpHeaders);
		
		return new ResponseEntity<>("{\"token\":\"" + jwt.getAccessToken() + "\"}", httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public ResponseEntity<String> join(@RequestBody UserInfoDTO userInfoDTO) {
		int result = 0;
		
		logger.info("Test POST /user/join, Data {}", userInfoDTO.toString());
		
		try {
			result = (int) userInfoServiceImpl.insertUserInfo(userInfoDTO);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			return new ResponseEntity<>("{\"errMsg\" : \"" + e.getMessage() + "\"}", null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("{\"result\":\"" + Integer.toString(result) + "\"}", null, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/join2", method = RequestMethod.POST)
	public ResponseEntity<String> join2(@RequestBody UserInfoDTO userInfoDTO) {
		int result = 0;
		
		logger.info("Test POST /user/join, Data {}", userInfoDTO.toString());
		
		//try {
			result = (int) userInfoServiceImpl.insertUserInfo2(userInfoDTO);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			
//			return new ResponseEntity<>("{\"errMsg\" : \"" + e.getMessage() + "\"}", null, HttpStatus.BAD_REQUEST);
//		}

		return new ResponseEntity<>("{\"result\":\"" + Integer.toString(result) + "\"}", null, HttpStatus.OK);
	}
	
	/*
	 * /user/logout 으로 요청이 왔을 때 사용됨
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(@RequestBody LoginDTO loginDTO) throws Exception {
		logger.info("Test POST /user/logout, Data {}", loginDTO.toString());
		
		return "LOGOUT";
	}
	
	/*
	 * /user/rest/json 으로 요청이 왔을 때 사용됨
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
	@PreAuthorize("hasRole('USER')")
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
