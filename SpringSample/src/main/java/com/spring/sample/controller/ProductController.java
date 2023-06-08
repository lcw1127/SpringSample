package com.spring.sample.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.sample.dto.ProductInfoDTO;
import com.spring.sample.service.ProductInfoServiceImpl;

/**
 * Handles requests for the application home page.
 */

@RestController
@RequestMapping(value = "/product")
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	private final ProductInfoServiceImpl productInfoServiceImpl;
	
	public ProductController(ProductInfoServiceImpl productInfoServiceImpl) {
		this.productInfoServiceImpl = productInfoServiceImpl;
	}
	
	/*
	 * /product 로 요청이 왔을 때 사용됨
	 **/
	@RequestMapping(value = "", produces = "application/json; charset=utf8", method = RequestMethod.GET)
	public ResponseEntity<String> getAllProductInfo() {
		List<ProductInfoDTO> productInfoList = null;
		
		logger.info("Test GET /product, Data");
		
		try {
			productInfoList = this.productInfoServiceImpl.selectAllProductInfo();
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			return new ResponseEntity<>("{\"errMsg\" : \"" + e.getMessage() + "\"}", null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("{\"result\":\"" + productInfoList.toString() + "\"}", null, HttpStatus.OK);
	}
	
	/*
	 * /product/add 로 요청이 왔을 때 사용됨
	 **/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<String> add(@RequestBody ProductInfoDTO productInfoDTO) {
		boolean result = true;
		
		logger.info("Test POST /product/add, Data {}", productInfoDTO.toString());
		
		try {
			result = this.productInfoServiceImpl.insertProductInfo(productInfoDTO);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			return new ResponseEntity<>("{\"errMsg\" : \"" + e.getMessage() + "\"}", null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("{\"result\":\"" + Boolean.toString(result) + "\"}", null, HttpStatus.OK);
	}
}
