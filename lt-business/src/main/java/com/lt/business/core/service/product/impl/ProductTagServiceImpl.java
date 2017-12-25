package com.lt.business.core.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lt.business.core.dao.product.IProductTagDao;
import com.lt.business.core.service.product.IProductTagService;
import com.lt.model.product.ProductTagInfo;
import com.lt.vo.user.UserProductSelectListVo;
import com.lt.vo.user.UserProductSelectVo;

@Service
public class ProductTagServiceImpl implements IProductTagService{

	@Autowired
	private IProductTagDao productTagDao;
	
	public List<ProductTagInfo> findProductList(){
		return productTagDao.selectProductTagList();
		
	}
	
	public List<UserProductSelectListVo> findProductByTag(int tagId){
		return productTagDao.selectProductByTagId(tagId);
	}
}
