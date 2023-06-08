package com.spring.sample.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.sample.dto.ProductInfoDTO;
import com.spring.sample.dto.PurchaseInfoDTO;

@Repository
public class PurchaseDAOImpl implements PurchaseDAO {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseDAOImpl.class);
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public PurchaseDAOImpl() {
	}
	
	@Override
	public void printQueryId(String queryId) {
		if(logger.isDebugEnabled())
		{
			logger.debug("\t QueryId \t: " + queryId);
		}
	}

	// 제품 조회
	@Override
	public ProductInfoDTO selectProductInfo(String queryId, String name) throws Exception {
		this.printQueryId(queryId);
		
		return this.sqlSession.selectOne(queryId, name);
	}

	// 수량 변경
	@Override
	public int updateProductQuantity(String queryId, Map<String, Object> param) throws Exception {
		this.printQueryId(queryId);
		
		return this.sqlSession.update(queryId, param);
	}

	// 제품 구매 정보 추가
	@Override
	public int insertPurchaseInfo(String queryId, PurchaseInfoDTO purchaseInfoDTO) throws Exception {
		this.printQueryId(queryId);
		
		return this.sqlSession.insert(queryId, purchaseInfoDTO);
	}
}
