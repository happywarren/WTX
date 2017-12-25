package com.lt.business.core.dao.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.model.product.ProductType;
import com.lt.vo.product.ProductVo;
import com.lt.vo.user.UserProductSelectListVo;


/**
 * 商品类型dao
 * @author jingwb
 *
 */
public interface IProductTypeDao {

	/**
	 * 查询商品类型集合
	 * @param vo
	 * @return
	 */
	public List<ProductType> selectProductTypes(ProductVo vo);
	
	/**
	 * 查询商品类型集合
	 * @param vo
	 * @param excludeProductTypeCode 排除的商品编号
	 * @return
	 */
	public List<ProductType> selectProductTypesByCondition(@Param("excludeProductTypeCode")String excludeProductTypeCode);
	
	
	public List<ProductType> selectProductTypeListByCondition(@Param("investorId")String investorId,@Param("excludeProductTypeCode")String excludeProductTypeCode);
	/**
	 * 根据商品类型查询
	 * @param id
	 * @return
	 */
	public List<UserProductSelectListVo> selectProductByTypeId(Integer id);
	
	/**
	 * 根据商品类型查询
	 * @param id
	 * @return
	 */
	public List<UserProductSelectListVo> selectProductForTypeId(@Param("userId")String userId,@Param("investorId")String investorId,@Param("id")Integer id);
}
