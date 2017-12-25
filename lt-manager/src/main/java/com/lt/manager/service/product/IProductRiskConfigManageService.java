package com.lt.manager.service.product;

import com.lt.manager.bean.product.ProductRiskConfigVO;
import com.lt.manager.service.BaseService;

import java.util.List;

/**
 * 描述: 商品风险杠杆相关配置服务
 *
 * @author lvx
 * @created 2017/7/19
 */
public interface IProductRiskConfigManageService extends BaseService<ProductRiskConfigVO, Integer> {

    public void insertBatch(List<ProductRiskConfigVO> list, Integer productId);

    public void deleteBatch(String ids);
}
