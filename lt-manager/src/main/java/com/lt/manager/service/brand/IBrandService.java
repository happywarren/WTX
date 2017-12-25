package com.lt.manager.service.brand;

import com.github.pagehelper.Page;
import com.lt.manager.bean.brand.BrandPage;
import com.lt.manager.bean.brand.BrandVo;

import java.util.List;

/**
 * Description: 品牌管理
 * Created by yanzhenyu on 2017/8/25.
 */
public interface IBrandService {

    Page<BrandPage> getBrandListPage(BrandVo brandPage);

    List<BrandPage> getBrandList(BrandVo brandPage);

    Integer getBrandCount(BrandVo brandPage);

    void addBrand(BrandVo brandPage);

    void deleteBrand(String brandId);

    void updateBrand(BrandVo brand);
}
