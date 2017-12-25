package com.lt.manager.service.impl.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.dao.product.ProductTradeConfigManageDao;
import com.lt.manager.service.product.IProductTradeConfigService;
import com.lt.model.product.ProductTradeConfig;

@Service
public class ProductTradeConfigServiceImpl implements IProductTradeConfigService{
	
	@Autowired
	private ProductTradeConfigManageDao productTradeConfigManageDao;
	/**
	 * 查询商品信息
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ProductTradeConfig getProductTradeConfigByProductId(Integer productId)throws Exception{
		ProductParamVO param= new ProductParamVO();
		param.setId(productId+"");
		return productTradeConfigManageDao.selectProTradeCfg(param);
	}
}
