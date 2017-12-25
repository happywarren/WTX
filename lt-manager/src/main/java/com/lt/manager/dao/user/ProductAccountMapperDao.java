package com.lt.manager.dao.user;


import com.lt.manager.bean.user.ProductAccountMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 
 * <br>
 * <b>功能：</b>ProductAccountMapperDao<br>
 */
public interface ProductAccountMapperDao {

	/**
	 * ProductAccountMapper查询接口
	 * @param param
	 * @return
	 */
	public List<ProductAccountMapper> selectProductAccountMapperPage(ProductAccountMapper param);
	
	/**
	 * ProductAccountMapper查询根据数量分页
	 * @param param
	 * @return
	 */
	public Integer selectProductAccountMapperCount(ProductAccountMapper param);
	
	
	/**
	 * ProductAccountMapper查询根据数量分页
	 * @param param
	 * @return
	 */
	public void insertProductAccountMapper(ProductAccountMapper param);
	
	/**
	 *ProductAccountMapper信息更改
	 * @param param
	 * @return
	 */
	public void updateProductAccountMapper(ProductAccountMapper param);
	
	/**
	 * ProductAccountMapper信息删除
	 * @param param
	 * @return
	 */
	public void deleteProductAccountMapper(String id);
	
	/**
	 * 根据ID获得ProductAccountMapper对象
	 */
	public ProductAccountMapper selectProductAccountMapperById(Integer id);
	/**
	 * 根据ID获得ProductAccountMapper对象
	 */
	public ProductAccountMapper selectProductAccountMapperBySecodeProId(Integer productId,String securityCode,String investorId);
	
	/**
	 *ProductAccountMapper信息更改
	 * @param param
	 * @return
	 */
	public void updateProductAccountMapperBySecurityCode(@Param("investorId") String investorId, @Param("securityCodeOld") String securityCodeOld, @Param("securityCodeNew") String securityCodeNew);
	/**
	 * 根据对象获得ProductAccountMapper满足条件列表
	 */
	public List<ProductAccountMapper> selectProductAccountMapper(ProductAccountMapper param);
	
	public void insertProAccMappers(List<ProductAccountMapper> list);

	/**
	 * ProductAccountMapper信息删除
	 * @param param
	 * @return
	 */
	public void deleteMapperBySecurityCodeAndInvestorId(String securityCode, String investorId);
}
