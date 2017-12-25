package com.lt.business.core.dao.product;

import com.lt.model.product.ProductRiskConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 查询商品风险杠杆配置
 *
 * @author lvx
 * @created 2017/7/18
 */
public interface IProductRiskConfigDao {

    List<ProductRiskConfig> queryProductRiskConfig(@Param("productId") Integer productId, @Param("riskLevel") Integer riskLevel);

    ProductRiskConfig selectProductRiskConfigByUserId(@Param("productId") Integer productId, @Param("userId") String userId);

}
