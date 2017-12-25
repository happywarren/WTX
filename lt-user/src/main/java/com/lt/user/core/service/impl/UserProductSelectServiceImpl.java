package com.lt.user.core.service.impl;

import com.lt.api.business.product.IProductApiService;
import com.lt.model.product.Product;
import com.lt.model.user.UserProductSelect;
import com.lt.user.core.dao.sqldb.IUserProductSelectDao;
import com.lt.user.core.service.IUserProductSelectService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.vo.user.UserProductSelectListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserProductSelectServiceImpl implements IUserProductSelectService {

    @Autowired
    private IUserProductSelectDao productSelectDao;
    @Autowired
    private IProductApiService productApiService;

    @Override
    public void addProductOptional(String userId, String productIds) {
        List<UserProductSelect> list;
        try {
            list = getList(userId, productIds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.FU00000);
        }
        productSelectDao.addObject(list);
    }

    private List<UserProductSelect> getList(String userId, String productIds) throws Exception {
        List<UserProductSelect> list = new ArrayList<UserProductSelect>();
        String[] strs = productIds.split(",");
        for (String string : strs) {
            UserProductSelect info = new UserProductSelect(userId, Integer.valueOf(string));
            Product product = productApiService.loadProduct(Integer.valueOf(string));
            if (product != null) {
                info.setProductShortCode(product.getShortCode());
            }
            list.add(info);
        }
        return list;
    }

    @Override
    @Transactional
    public void updateProductOptional(String userId, String productIds) {
        try {
            productSelectDao.deleteObjectByUserId(userId);
            this.addProductOptional(userId, productIds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.FU00000);
        }

    }

    @Override
    public List<UserProductSelectListVo> selectProductOptional(String userId) {
        return productSelectDao.selectObjectByUserId(userId);

    }

    @Override
    public void deleteProductOptional(String userId, String productCode) {
        for (String subProductCode : productCode.split(",")) {
            if (StringTools.isNotEmpty(subProductCode)) {
                productSelectDao.deleteObjectByUserIdAndCode(userId, subProductCode);
            }
        }
    }

    @Override
    public void addProductOptional(String userId, Integer productId) {
        UserProductSelect info = new UserProductSelect(userId, productId);
        Product product = productApiService.loadProduct(productId);
        if (product != null) {
            info.setProductShortCode(product.getShortCode());
        }
        productSelectDao.saveObject(info);

    }

    @Override
    public UserProductSelect selectProductOptional(String userId,
                                                   String shortCode) {
        // TODO Auto-generated method stub
        return productSelectDao.selectProductOptional(userId, shortCode);
    }

    @Override
    public List<UserProductSelectListVo> selectProductForInvestorGroup(
            String userId, String investorId) {
        return productSelectDao.selectProductForInvestorGroup(userId, investorId);
    }

	@Override
	public List<UserProductSelectListVo> selectProductOptionalByCondition(String userId,
			String excludeProductTypeCode) {
		  return productSelectDao.selectObjectByCondition(userId, excludeProductTypeCode);
	}

	@Override
	public List<UserProductSelectListVo> selectProductForInvestorGroup(String userId, String investorId,
			String excludeProductTypeCode) {
		return productSelectDao.selectProductForInvestorGroup(userId, investorId,excludeProductTypeCode);
	}
}
