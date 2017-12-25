package com.lt.manager.dao.product;

import java.util.List;

import com.lt.manager.bean.product.ProductParamVO;
import com.lt.model.product.ProductTagInfo;
import com.lt.model.product.ProductTagMapper;

/**
 * 商品标签管理dao
 * @author jingwb
 *
 */
public interface ProductTagManageDao {

	/**
	 * 添加商品标签
	 * @param param
	 */
	public void insertProductTag(ProductParamVO param);
	
	/**
	 * 批量添加商品与标签中间表信息
	 * @param list
	 */
	public void insertProTagMappers(List<ProductTagMapper> list);
	
	/**
	 * 编辑商品标签
	 * @param param
	 */
	public void updateProductTag(ProductParamVO param);
	
	/**
	 * 删除商品标签
	 * @param param
	 */
	public void deleteProductTag(ProductParamVO param);
	
	/**
	 * 查询商品标签信息集
	 * @param param
	 * @return
	 */
	public List<ProductTagInfo> selectProductTagList();
	
	public List<ProductTagInfo> selectProductTagListByProId(Integer proId);
	
	/**
	 * 删除商品与标签中间信息
	 * @param param
	 */
	public void deleteProTagMappers(ProductParamVO param);
}
