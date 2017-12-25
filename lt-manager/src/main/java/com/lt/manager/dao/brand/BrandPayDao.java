package com.lt.manager.dao.brand;

import com.lt.manager.bean.brand.BrandPage;
import com.lt.model.brand.BrandPayInfo;

import java.util.List;

/**
 * Description: 品牌管理
 * Created by yanzhenyu on 2017/8/25.
 */
public interface BrandPayDao {
    //获取 某个 品牌 id 所有对应的支付渠道
    List<BrandPayInfo> findByBrandId(String brandId);

    //增加品牌支付对应关系
    void insertList(List<BrandPayInfo> brandPayInfos);

    //按品牌 id 删除 所有支付渠道
    void deleteByBrandId(String brandId);
}
