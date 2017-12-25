package com.lt.manager.service.product;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ProductParamVO;
import com.lt.model.product.ProductType;
import com.lt.util.utils.model.Response;

import java.util.List;

/**
 * 商品类型管理service
 * @author jingwb
 *
 */
public interface IProductTypeManageService {

	/**
	 * 添加商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void addProductType(ProductParamVO param) throws Exception;
	
	/**
	 * 编辑商品类型，该方法不支持修改排序号
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editProductType(ProductParamVO param) throws Exception;
	
	/**
	 * 该方法仅修改排序号
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editProductTypeSort(ProductParamVO param) throws Exception;

	/**
	 * 删除商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void removeProductType(ProductParamVO param) throws Exception;

	/**
	 * 获取商品类型集--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<ProductType> queryProductTypePage(ProductParamVO param) throws Exception;

	/**
	 * 获取商品类型集
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ProductType> queryProductType(ProductParamVO param) throws Exception;

	/**
	 * 获取商品类型数量
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer queryProductTypeCount(ProductParamVO param) throws Exception;
	
	public void test() throws Exception;
	
	public void test1() throws Exception;
}
