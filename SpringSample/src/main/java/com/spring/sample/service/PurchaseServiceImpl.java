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
		
		try {
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
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			/*
			 * try ~ catch 로 잡아 throw new Exception() 수행 시
			 * Checked Exception 으로 rollback이 정상적으로 동작하지 않음
			 *  -> Checked Exception : 반드시 에러 처리(try ~ catch 또는 throw) 해야 하는 것들  
			 * 이 경우 @Transactional 에 (rollbackFor = Exception.class)를 추가해야 rollback이 수행 됨
			 * 
			 * throw new RuntimeException() 사용시 rollback 수행 됨
			 * -> Unchecked Exception : Runtime 중 발생하는 것들(Null 참조, Out of Range 등)
			 */
			throw new RuntimeException("구매 등록 실패");
		}

		return true;
	}
}
