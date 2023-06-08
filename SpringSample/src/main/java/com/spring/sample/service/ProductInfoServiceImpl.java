package com.spring.sample.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.spring.sample.dao.ProductInfoDAO;
import com.spring.sample.dto.ProductInfoDTO;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
	private static final Logger logger = LoggerFactory.getLogger(ProductInfoServiceImpl.class);
	
	/*
	 * @Transactional을 사용해 자동으로 트랜잭션을 관리할 수 있도록 하기 위해
	 * 구현체인 ProductInfoDAOImpl를 사용하지 않고
	 * Interface인 ProductInfoDAO를 사용
	 */
	//@Autowired
	private final ProductInfoDAO productInfoDAO;
	
	public ProductInfoServiceImpl(ProductInfoDAO productInfoDAO) {
		this.productInfoDAO = productInfoDAO;
	}
		
	// 모든 제품 조회
	@Override
	public List<ProductInfoDTO> selectAllProductInfo() throws Exception {
		List<ProductInfoDTO> productInfoList = null;
		
		logger.debug("this.productInfoDAO {}", this.productInfoDAO);
		
		productInfoList = this.productInfoDAO.selectAllProductInfo("product_info_sql.selectAllProductInfo");
		logger.info("Product Info List {}", productInfoList);
		
		return productInfoList;
	}

	// 제품 정보 추가
	@Override
	public boolean insertProductInfo(ProductInfoDTO productInfoDTO) throws Exception {
		int result = 0;
		
		logger.debug("this.productInfoDAO {}", this.productInfoDAO);
		
		try {
			result = (int)this.productInfoDAO.insertProductInfo(productInfoDTO);
			logger.info("Insert Product Info Result {}", result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			logger.error("Insert ProductInfo Error");
			
			throw new Exception("Insert ProductInfo Failure");
		}
		
		return true; 
	}
}
