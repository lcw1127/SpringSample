package com.spring.sample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.sample.dto.PurchaseInfoDTO;
import com.spring.sample.service.PurchaseService;

/**
 * Handles requests for the application home page.
 */

@RestController
@RequestMapping(value = "/purchase")
public class PurchaseController {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
	
	//@Autowired
	private final PurchaseService purchaseService;
	
	public PurchaseController(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}
//	
//	public PurchaseController(PurchaseService purchaseService) {
//		this.purchaseService = purchaseService;
//	}
	
	/*
	 * /purchase 로 요청이 왔을 때 사용됨
	 * Service에 대해 Transaction 관리를 하도록 사용
	 * Service 구현체인 ServiceImpl의 purchaseProduct 함수에도 @Transactional 을 붙여줘야 관리가 됨
	 **/
	@Transactional
	@RequestMapping(value = "", produces = "application/json; charset=utf8", method = RequestMethod.POST)
	public ResponseEntity<String> getAllProductInfo(@RequestBody PurchaseInfoDTO purchaseInfo) {
		boolean result = true;
		
		logger.info("Test POST /purchase, PurchaseInfo {}", purchaseInfo);
		
		try {
			result = this.purchaseService.purchaseProduct(purchaseInfo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			return new ResponseEntity<>("{\"errMsg\" : \"Purchase Product Failure\"}", null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("{\"result\":\"" + Boolean.toString(result) + "\"}", null, HttpStatus.OK);
	}
}
