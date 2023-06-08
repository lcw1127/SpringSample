package com.spring.sample.dao;

import java.util.List;

import com.spring.sample.dto.ProductInfoDTO;

public interface ProductInfoDAO {
	public void printQueryId(String queryId);
	
	// 모든 제품 조회
	public List<ProductInfoDTO> selectAllProductInfo(String queryId) throws Exception;
	
	// 제품 정보 추가
	public int insertProductInfo(ProductInfoDTO productInfoDTO) throws Exception;
}
