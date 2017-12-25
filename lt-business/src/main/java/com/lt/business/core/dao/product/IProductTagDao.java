package com.lt.business.core.dao.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.model.product.ProductTagInfo;
import com.lt.vo.user.UserProductSelectListVo;
import com.lt.vo.user.UserProductSelectVo;

/**
 * 商品标签管理dao
 * @author jingwb
 *
 */
public interface IProductTagDao {

	
	/**
	 * 查询商品标签信息集
	 * @param param
	 * @return
	 */
	public List<ProductTagInfo> selectProductTagList();
	
	/**
	 * 查询标签信息
	 * @param proId 商品ID
	 * @return
	 */
	public List<ProductTagInfo> selectProductTagListByProId(Integer proId);
	
	/**
	 * 查询商品信息  
	 * @param tagId 标签ID
	 * @return
	 */
	public List<UserProductSelectListVo> selectProductByTagId(Integer tagId);
	
	public List<UserProductSelectListVo> selectProductForTagId(@Param("tagId")Integer tagId,@Param("userId")String userId,@Param("investorId")String investorId);
	
}
