package com.spring.sample.dao;

import java.util.Map;

import com.spring.sample.dto.ProductInfoDTO;
import com.spring.sample.dto.PurchaseInfoDTO;

public interface PurchaseDAO {
	public void printQueryId(String queryId);
	
	// 제품 조회
	public ProductInfoDTO selectProductInfo(String queryId, String name) throws Exception;
	
	// 수량 변경
	public int updateProductQuantity(String queryId, Map<String, Object> param) throws Exception;
	
	// 제품 구매 정보 추가
	public int insertPurchaseInfo(String queryId, PurchaseInfoDTO purchaseInfoDTO) throws Exception;
}
