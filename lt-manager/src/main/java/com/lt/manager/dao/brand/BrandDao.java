package com.lt.manager.dao.brand;

import com.lt.manager.bean.brand.BrandBean;
import com.lt.manager.bean.brand.BrandPage;
import com.lt.manager.bean.brand.BrandVo;
import com.lt.model.user.charge.ChargeChannelInfo;

import java.util.List;

/**
 * Description: 品牌管理
 * Created by yanzhenyu on 2017/8/25.
 */
public interface BrandDao {

    List<BrandPage> getBrandListPage(BrandVo brandPage);
    
    List<BrandBean> getBrandList(BrandVo brand);

    List<ChargeChannelInfo> findPayChannelByBrandId(String brandId);

    String findBrandNameByBrandId(String brandId);

    Integer getBrandCount(BrandVo brandPage);

    void addBrand(BrandVo brandPage);

    void deleteBrand(BrandVo brandPage);

    void updateBrand(BrandVo brandPage);

}
