package com.lt.business.core.service.product.impl;

import java.util.List;

import com.lt.vo.defer.ProductDeferTimeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.business.product.IProductTimeConfigApiService;
import com.lt.business.core.dao.product.IProductTimeConfigDao;
import com.lt.business.core.service.product.IProductTimeConfigService;
import com.lt.model.product.ProductTimeConfig;

@Service
public class ProductTimeConfigApiServiceImpl implements IProductTimeConfigApiService {

	@Autowired
	private IProductTimeConfigService productTimeConfigService;
	
	@Override
	public List<ProductTimeConfig> findProductTimeConfigByProductId(
			Integer productId) {
		return productTimeConfigService.findProductTimeConfigByProductId(productId);
	}

	@Override
	public List<ProductDeferTimeInfoVo> queryAllProductDeferTime() {
		return productTimeConfigService.queryAllProductDeferTime();
	}

	@Override
	public List<ProductDeferTimeInfoVo> queryAllProductClearTime() {
		return productTimeConfigService.queryAllProductClearTime();
	}

}
