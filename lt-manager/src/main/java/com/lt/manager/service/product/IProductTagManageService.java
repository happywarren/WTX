package com.lt.manager.service.product;

import java.util.List;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ProductParamVO;
import com.lt.model.product.ProductTagInfo;
import com.lt.util.utils.model.Response;

/**
 * 商品标签管理service
 * @author jingwb
 *
 */
public interface IProductTagManageService {

	/**
	 * 商品标签添加
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String addProductTag(ProductParamVO param) throws Exception;

	/**
	 * 商品标签修改
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editProductTag(ProductParamVO param) throws Exception;
	
	/**
	 * 商品标签删除
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void removeProductTag(ProductParamVO param) throws Exception;
	
	/**
	 * 查询商品信息--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ProductTagInfo> queryProductTagList() throws Exception;
}
