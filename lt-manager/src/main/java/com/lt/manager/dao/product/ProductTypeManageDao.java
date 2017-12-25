package com.lt.manager.dao.product;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.product.ProductParamVO;
import com.lt.model.product.ProductType;

/**
 * 商品类型管理dao
 * @author jingwb
 *
 */
public interface ProductTypeManageDao {

	/**
	 * 获取商品类型集--分页
	 * @param param
	 * @return
	 */
	public List<ProductType> selectProductTypePage(ProductParamVO param);
	
	/**
	 * 获取商品类型数量
	 * @param param
	 * @return
	 */
	public Integer selectProductTypeCount(ProductParamVO param);
	
	/**
	 * 添加商品类型
	 * @param param
	 */
	public void insertProductType(ProductParamVO param);
	
	/**
	 * 修改商品类型
	 * @param param
	 */
	public void updateProductType(ProductParamVO param);
	
	/**
	 * 修改排序均加一
	 */
	public void updateSort();
	
	/***
	 * 删除商品类型
	 * @param param
	 */
	public void deleteProductType(ProductParamVO param);
	
	/**
	 * 查询商品类型
	 * @param param
	 * @return
	 */
	public ProductType selectProductTypeOne(ProductParamVO param);
	
	/**
	 * 排序大改小
	 * @param map
	 */
	public void updateSortForGTL(Map<String,Object> map);
	
	/**
	 * 排序小改大
	 * @param map
	 */
	public void updateSortForLTG(Map<String,Object> map);
	
	public void updateSortToL(String oldSort);
	
	public int test();
	
	public int test1();
}
