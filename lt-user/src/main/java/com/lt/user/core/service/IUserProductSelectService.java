package com.lt.user.core.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.model.user.UserProductSelect;
import com.lt.vo.user.UserProductSelectListVo;
import com.lt.vo.user.UserProductSelectVo;

public interface IUserProductSelectService {

	/**
	 * 批量自选新增
	 * @param userId
	 * @param productIds
	 */
	public void addProductOptional(String userId, String productIds);

	/**
	 * 單個自选新增
	 * @param userId
	 * @param productId
	 */
	void addProductOptional(String userId, Integer productId);
	/**
	 * 更新自选商品
	 * @param userId
	 * @param productIds
	 */
	public void updateProductOptional(String userId, String productIds);

	/**
	 * 查询自选商品
	 * @param userId
	 */
	public List<UserProductSelectListVo> selectProductOptional(String userId);

	
	/**
	 * 查询自选商品
	 * @param userId
	 * @param excludeProductTypeCode 排除商品类型编码
	 */
	public List<UserProductSelectListVo> selectProductOptionalByCondition(String userId,String excludeProductTypeCode);
	
	/**
	 * 查询用户自选商品信息
	 * @param userId
	 * @param productId
	 * @return
	 */
	public UserProductSelect selectProductOptional(String userId,String shortCode);
	
	/**
	 * 删除自选商品
	 * @param userId
	 */
	void deleteProductOptional(String userId, String productCode);
	
	/**
	 * 查询券商分组后展示自选商品
	 * @param userId
	 * @return
	 */
	public List<UserProductSelectListVo> selectProductForInvestorGroup(@Param("userId")String userId ,@Param("investorId")String investorId );
	


	/**
	 * 查询券商分组后展示自选商品
	 * @param userId
	 * @param excludeProductTypeCode 排除商品类型编码
	 * @return
	 */
	public List<UserProductSelectListVo> selectProductForInvestorGroup(@Param("userId")String userId ,@Param("investorId")String investorId ,@Param("excludeProductTypeCode")String excludeProductTypeCode);
}
