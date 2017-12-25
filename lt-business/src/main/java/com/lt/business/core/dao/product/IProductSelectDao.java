package com.lt.business.core.dao.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.model.user.UserProductSelect;
import com.lt.vo.user.UserProductSelectListVo;

public interface IProductSelectDao {

	/**
	 * 批量新增
	 * @param list
	 */
	public void addObject(List<UserProductSelect> list);
	
	/**
	 * 新增
	 * @param productSelect
	 */
	public void saveOject(UserProductSelect productSelect);
	
	/**
	 * 删除
	 * @param id
	 */
	public void deleteObjectByUserId(String userId);
	
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
	 * 查询券商分组后展示自选商品
	 * @param userId
	 * @return
	 */
	public List<UserProductSelectListVo> selectProductForInvestorGroup(@Param("userId")String userId ,@Param("investorId")String investorId );
}
