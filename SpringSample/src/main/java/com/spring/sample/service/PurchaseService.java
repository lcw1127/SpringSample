package com.spring.sample.service;

import com.spring.sample.dto.PurchaseInfoDTO;

public interface PurchaseService {
	// 제품 구매
	public boolean purchaseProduct(PurchaseInfoDTO purchaseInfo) throws Exception;
}
