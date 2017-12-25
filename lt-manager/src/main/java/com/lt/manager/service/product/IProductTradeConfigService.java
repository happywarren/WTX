package com.lt.manager.service.product;

import com.lt.model.product.ProductTradeConfig;

public interface IProductTradeConfigService {

	ProductTradeConfig getProductTradeConfigByProductId(Integer productId)
			throws Exception;

}
