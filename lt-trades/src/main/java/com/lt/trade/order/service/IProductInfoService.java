package com.lt.trade.order.service;

import com.lt.model.product.Product;
import com.lt.model.product.ProductTimeConfig;
import com.lt.vo.product.ProductVo;

import java.util.List;

/**
 * 商品基础数据业务接口
 *
 * Created by sunch on 2016/12/16.
 */
public interface IProductInfoService {

    /**
     * 
     * TODO 查询产品
     * @author XieZhibing
     * @date 2017年2月8日 下午2:57:30
     * @param productCode
     * @return
     */
    public ProductVo queryProduct(String productCode);
    /**
     * 
     * TODO 查询并校验产品
     * @author XieZhibing
     * @date 2017年2月8日 下午2:40:42
     * @param productCode
     * @return
     */
    public ProductVo queryCheckProduct(String productCode);
}
