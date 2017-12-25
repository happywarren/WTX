package com.lt.manager.dao.product;

import com.lt.manager.bean.product.ProductRiskConfigVO;
import com.lt.manager.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 商品风险杠杆相关配置Dao
 *
 * @author lvx
 * @created 2017/7/19
 */
public interface ProductRiskConfigManageDao extends BaseDao<ProductRiskConfigVO,Integer> {
    public void insertBatch(List<ProductRiskConfigVO> list);

    public void deleteBatch(@Param("ids") String ids);
}
