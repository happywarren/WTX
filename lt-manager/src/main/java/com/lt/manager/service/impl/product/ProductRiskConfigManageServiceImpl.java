package com.lt.manager.service.impl.product;

import com.lt.manager.bean.product.ProductRiskConfigVO;
import com.lt.manager.dao.BaseDao;
import com.lt.manager.dao.product.ProductRiskConfigManageDao;
import com.lt.manager.service.impl.BaseServiceImpl;
import com.lt.manager.service.product.IProductRiskConfigManageService;
import com.lt.util.utils.StringTools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述: 商品风险杠杆相关配置服务实现类
 *
 * @author lvx
 * @created 2017/7/19
 */
@Service
public class ProductRiskConfigManageServiceImpl extends BaseServiceImpl<ProductRiskConfigVO, Integer> implements IProductRiskConfigManageService {

    @Resource
    private ProductRiskConfigManageDao productRiskConfigManageDao;

    @Override
    protected BaseDao<ProductRiskConfigVO, Integer> getDao() {
        return productRiskConfigManageDao;
    }

    @Override
    public void insertBatch(List<ProductRiskConfigVO> list, Integer productId) {
        if(StringTools.isNotEmpty(productId)){
            for(ProductRiskConfigVO vo:list){
                vo.setProductId(productId);
            }
        }
        productRiskConfigManageDao.insertBatch(list);
    }

    @Override
    public void deleteBatch(String ids) {
        productRiskConfigManageDao.deleteBatch(ids);
    }
}
