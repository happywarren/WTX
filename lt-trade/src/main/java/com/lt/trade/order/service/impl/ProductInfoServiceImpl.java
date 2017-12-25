package com.lt.trade.order.service.impl;

import com.lt.api.business.product.IProductApiService;
import com.lt.enums.product.ProductMarketEnum;
import com.lt.model.product.Product;
import com.lt.model.product.ProductTimeConfig;
import com.lt.trade.order.dao.ProductInfoDao;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.vo.product.ProductVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品基础数据业务实现
 *
 * Created by sunch on 2016/12/15.
 */
@Service
public class ProductInfoServiceImpl implements IProductInfoService {
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ProductInfoServiceImpl.class);
    @Autowired
    private ProductInfoDao productInfoDao;
    @Autowired
    private IProductApiService productApiService;
    
    /**
     * 
     * TODO 查询内盘/外盘所有上架商品(内盘: 0, 外盘: 1)
     * @author sunch
     * @date 2016年12月16日 下午2:40:02
     * @see com.lt.trade.order.service.IProductInfoService#queryProductInfo(java.lang.Integer)
     * @param plate
     * @return
     */
    @Override
	public List<Product> queryProductInfo(Integer plate) {
        return productInfoDao.queryProductInfo(plate, 1);
    }

    /**
     * 
     * TODO 查询商品时间配置信息
     * @author sunch
     * @date 2016年12月16日 下午2:40:02
     * @see com.lt.trade.order.service.IProductInfoService#queryProductTime(java.lang.Integer)
     * @param productId
     * @return
     */
    @Override
	public List<ProductTimeConfig> queryProductTime(Integer productId) {
        return productInfoDao.queryProductTime(productId);
    }

    
	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月8日 下午2:57:50
	 * @see com.lt.trade.order.service.IProductInfoService#queryProduct(java.lang.String)
	 * @param productCode
	 * @return
	 */
	@Override
	public ProductVo queryProduct(String productCode) {
		// TODO Auto-generated method stub
		//产品
		ProductVo productVo = productApiService.getProductInfo(productCode);
		if(productVo == null){
			//商品未找到
			throw new LTException(LTResponseCode.PR00002);
		}
		return productVo;
	}

	/** 
	 * 查询并校验产品
	 * @author XieZhibing
	 * @date 2017年2月8日 下午2:41:12
	 * @see com.lt.trade.order.service.IProductInfoService#queryCheckProduct(java.lang.String)
	 * @param productCode
	 * @return
	 */
	@Override
	public ProductVo queryCheckProduct(String productCode) {
		//产品
		ProductVo productVo = productApiService.getProductInfo(productCode);
		if(productVo == null){
			//商品未找到
			throw new LTException(LTResponseCode.PR00002);
		}
		
		//商品市场状态判断
		Integer marketStatus = productVo.getMarketStatus();
		if (ProductMarketEnum.ONLY_LIQUIDATION.getValue() != marketStatus
				&& ProductMarketEnum.START_TRADING.getValue() != marketStatus) {
			//商品市场状态判断
			logger.error("商品:{}市场状态marketStatus:{}", productCode, marketStatus);
			throw new LTException(LTResponseCode.PR00006);
		}
		
		//商品上下架状态判断
		Integer status = productVo.getStatus();
		if (ProductMarketEnum.PRODUCT_STATUS_UP.getValue() != status) {
			logger.error("商品:{}上架状态status:{}", productCode, status);
			throw new LTException(LTResponseCode.PR00008);
		}
		
		return productVo;
	}

}
