package com.lt.manager.service.impl.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.dao.product.ProductTagManageDao;
import com.lt.manager.service.product.IProductTagManageService;
import com.lt.model.product.ProductTagInfo;
import com.lt.util.utils.model.Response;

/**
 * 商品标签管理service实现类
 * @author jingwb
 *
 */
@Service
public class ProductTagManageServiceImpl implements IProductTagManageService{

	@Autowired
	private ProductTagManageDao productTagManageDao;
	
	@Override
	public String addProductTag(ProductParamVO param) throws Exception {
		productTagManageDao.insertProductTag(param);
		return param.getId().toString();
	}

	@Override
	public void editProductTag(ProductParamVO param) throws Exception {
		productTagManageDao.updateProductTag(param);
	}

	@Override
	public void removeProductTag(ProductParamVO param) throws Exception {
		productTagManageDao.deleteProductTag(param);
	}

	@Override
	public List<ProductTagInfo> queryProductTagList()
			throws Exception {
		return productTagManageDao.selectProductTagList();
	}

}
