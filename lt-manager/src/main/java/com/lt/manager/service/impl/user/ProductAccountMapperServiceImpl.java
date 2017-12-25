package com.lt.manager.service.impl.user;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.ProductAccountMapper;
import com.lt.manager.dao.user.ProductAccountMapperDao;
import com.lt.manager.service.user.IProductAccountMapperService;

/**
 * 
 * <br>
 * <b>功能：</b>ProductAccountMapperService<br>
 */
@Service("productAccountMapperService")
public class  ProductAccountMapperServiceImpl  implements IProductAccountMapperService {
  private final static Logger log= Logger.getLogger(ProductAccountMapperServiceImpl.class);
	
		
	@Autowired
	private ProductAccountMapperDao productAccountMapperDao;
	
	
	@Override
	public List<ProductAccountMapper> queryProductAccountMapperPage(ProductAccountMapper param)
			throws Exception {
		
		return productAccountMapperDao.selectProductAccountMapper(param);
	}

	@Override
	public Integer queryProductAccountMapperCount(ProductAccountMapper param) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addProductAccountMapper(ProductAccountMapper param) throws Exception {
		productAccountMapperDao.insertProductAccountMapper(param);
	}

	@Override
	public void editProductAccountMapper(ProductAccountMapper param) throws Exception {
		productAccountMapperDao.updateProductAccountMapper(param);
	}

	@Override
	public void removeProductAccountMapper(String id) throws Exception {
		productAccountMapperDao.deleteProductAccountMapper(id);
	}

	@Override
	public List<ProductAccountMapper> findProductAccountMapper(ProductAccountMapper param) {
		return productAccountMapperDao.selectProductAccountMapper(param);
	}
	
	@Override
	public ProductAccountMapper getProductAccountMapper(Integer id) {
		return productAccountMapperDao.selectProductAccountMapperById(id);
	}
	
	@Override
	public ProductAccountMapper getProductAccountMapperBySecodeProId(Integer productId,String securityCode,String investorId) {
		return productAccountMapperDao.selectProductAccountMapperBySecodeProId(productId,securityCode,investorId);
	}
	
	@Override
	public void editProductAccountMapperBySecurityCode(String investorId, String securityCodeOld,String securityCodeNew)  {
		 productAccountMapperDao.updateProductAccountMapperBySecurityCode(investorId, securityCodeOld,securityCodeNew);
	}
	
}
