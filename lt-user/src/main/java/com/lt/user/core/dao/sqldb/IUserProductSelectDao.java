package com.lt.user.core.dao.sqldb;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.model.user.UserProductSelect;
import com.lt.vo.user.UserProductSelectListVo;

public interface IUserProductSelectDao {

	/**
	 * 批量新增
	 * @param list
	 */
	public void addObject(List<UserProductSelect> list);
	
	/**
	 * 新增
	 * @param productSelect
	 */
	public void saveObject(UserProductSelect productSelect);
	
	/**
	 * 删除
	 * @param id
	 */
	public void deleteObjectByUserId(String userId);
	
	/**
	 * 删除用户自选品种
	 * @param userId
	 * @param productCode
	 */
	public void deleteObjectByUserIdAndCode(@Param("userId")String userId ,@Param("productCode") String productCode);
	/**
	 * 批量删除
	 * @param list
	 */
	public void deleteObject(@Param("userId")String userId ,@Param("id") Integer[] id);
	
	/**
	 * 查询用户自选股
	 * @return
	 */
	public List<UserProductSelectListVo> selectObjectByUserId(String userId);
	
	/**
	 * 查询用户自选股
	 * @return
	 */
	public List<UserProductSelectListVo> selectObjectByCondition(@Param("userId")String userId,@Param("excludeProductTypeCode")String excludeProductTypeCode);
	
	/**
	 * 查询用户自选股
	 * @param userId
	 * @param productId
	 * @return
	 */
	public UserProductSelect selectProductOptional(@Param("userId")String userId,
			@Param("shortCode")String shortCode);

	/**
	 * 查询券商分组后展示自选商品
	 * @param userId
	 * @param investorId
	 * @return
	 */
	public List<UserProductSelectListVo> selectProductForInvestorGroup(
			@Param("userId")String userId, @Param("investorId")String investorId);
	
	
	/**
	 * 查询券商分组后展示自选商品
	 * @param userId
	 * @param investorId
	 * @return
	 */
	public List<UserProductSelectListVo> selectProductForInvestorGroup(
			@Param("userId")String userId,
			@Param("investorId")String investorId,
			@Param("excludeProductTypeCode")String excludeProductTypeCode);
}
