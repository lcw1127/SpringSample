package com.spring.sample.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.sample.dao.PurchaseDAOImpl;
import com.spring.sample.dto.ProductInfoDTO;
import com.spring.sample.dto.PurchaseInfoDTO;

@Service
public class PurchaseServiceImpl implements PurchaseService {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);
	
	private final PurchaseDAOImpl purchaseDAOImpl;
	
	public PurchaseServiceImpl(PurchaseDAOImpl purchaseDAOImpl) {
		this.purchaseDAOImpl = purchaseDAOImpl;
	}

	// 제품 구매 정보 등록
	/*
	 * Transaction 관리가 되도록 @Transactional 사용
	 */
	@Transactional
	@Override
	public boolean purchaseProduct(PurchaseInfoDTO purchaseInfoDTO) throws Exception {
		ProductInfoDTO productInfo = null;
		
		int result = 0;
		
		// 제품 조회
		productInfo = purchaseDAOImpl.selectProductInfo("product_info_sql.selectProductInfo", purchaseInfoDTO.getProductName());
		logger.info("Get Product Info {} Purchase Quantity {}", productInfo, purchaseInfoDTO.getQuantity());
		
		// 수량 확인
		if ((productInfo.getQuantity() - purchaseInfoDTO.getQuantity()) < 0) {
			throw new Exception("Not Enough Quantity");
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("name", purchaseInfoDTO.getProductName());
		param.put("quantity", purchaseInfoDTO.getQuantity());
		
		// 수량 변경
		result = purchaseDAOImpl.updateProductQuantity("product_info_sql.updateProductQuantity", param);
		logger.info("Update Product Quantity Result {}", result);
		
		// 구매 정보 등록
		result = purchaseDAOImpl.insertPurchaseInfo("purchase_sql.insertPurchaseInfo", purchaseInfoDTO);
		logger.info("Update Product Quantity Result {}", result);

		return true;
	}
}
