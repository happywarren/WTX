package com.lt.trade.order.dao;

import com.lt.model.product.Product;
import com.lt.model.product.ProductTimeConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品基础信息数据接口
 *
 * Created by sunch on 2016/12/15.
 */
public interface ProductInfoDao {

    public List<Product> queryProductInfo(@Param("plate") Integer plate, @Param("status") Integer status);

    public List<ProductTimeConfig> queryProductTime(Integer productId);

}
