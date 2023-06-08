package com.spring.sample.service;

import java.util.List;

import com.spring.sample.dto.ProductInfoDTO;

public interface ProductInfoService {
	// 모든 제품 조회
	public List<ProductInfoDTO> selectAllProductInfo() throws Exception;
	
	// 제품 정보 추가
	public boolean insertProductInfo(ProductInfoDTO productInfoDTO) throws Exception;
}
