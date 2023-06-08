package com.spring.sample.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.sample.dto.ProductInfoDTO;

@Repository
public class ProductInfoDAOImpl implements ProductInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(ProductInfoDAOImpl.class);
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public ProductInfoDAOImpl() {
		logger.info("ProductInfoDAOImpl Constructor");
	}
	
	@Override
	public void printQueryId(String queryId) {
		if(logger.isDebugEnabled())
		{
			logger.debug("\t QueryId \t: " + queryId);
		}
	}

	// 모든 제품 조회
	@Override
	public List<ProductInfoDTO> selectAllProductInfo(String queryId) throws Exception
	{
		this.printQueryId(queryId);
		
		return this.sqlSession.selectList(queryId);
	}

	/*
	 * @Transactional을 사용해 자동으로 트랜잭션을 관리할 수 있도록 함
	 */
	// 제품 정보 추가
	@Transactional
	@Override
	public int insertProductInfo(ProductInfoDTO productInfoDTO) throws Exception
	{
		String queryId = "";
		int result = 0;
		
		this.printQueryId(queryId);
		
		// 제품 등록
		queryId = "product_info_sql.insertProductInfo";
		logger.info("Before Insert Product Info");
		result = this.sqlSession.insert(queryId, productInfoDTO);
		logger.info("Insert Product Info Result {}", result);
		
		// 제품 수량 및 금액 등록
		queryId = "product_info_sql.insertProductStock";
		logger.info("Before Insert Product Stock");
		result = this.sqlSession.insert(queryId, productInfoDTO);
		logger.info("Insert Product Stock Result {}", result);
		
		return result;
	}
}
