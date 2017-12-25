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
     * TODO 查询内盘/外盘所有上架商品(内盘: 0, 外盘: 1)
     * @author sunch
     * @date 2016年12月16日 下午2:40:02
     * @param plate
     * @return
     */
    public List<Product> queryProductInfo(Integer plate);

    /**
     * 
     * TODO 查询商品时间配置信息
     * @author sunch
     * @date 2016年12月16日 下午2:40:02
     * @param productId
     * @return
     */
    public List<ProductTimeConfig> queryProductTime(Integer productId);
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
