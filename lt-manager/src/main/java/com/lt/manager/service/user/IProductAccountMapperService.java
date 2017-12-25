package com.lt.manager.service.user;

import java.util.List;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.ProductAccountMapper;

/**
 * 
 * <br>
 * <b>功能：</b>ProductAccountMapperService<br>
 */
public interface IProductAccountMapperService {

/**
	 * 查询ProductAccountMapper--用于分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public List<ProductAccountMapper> queryProductAccountMapperPage(ProductAccountMapper param) throws Exception;
	
	/**
	 * ProductAccountMapper--数量用于分页
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public Integer queryProductAccountMapperCount(ProductAccountMapper param) throws Exception;
	
	/**ProductAccountMapper--用于编辑
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void editProductAccountMapper(ProductAccountMapper param) throws Exception;
	
	/**ProductAccountMapper--用于添加
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void addProductAccountMapper(ProductAccountMapper param) throws Exception;
	
	/**
	 *ProductAccountMapper--用于移除
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void removeProductAccountMapper(String id) throws Exception;
	/**
	 *ProductAccountMapper--用于获得对象
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public ProductAccountMapper getProductAccountMapper(Integer id) throws Exception;
	
	/**
	 *ProductAccountMapper--用于获得对象
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	public ProductAccountMapper getProductAccountMapperBySecodeProId(Integer productId,String securityCode,String investorId) throws Exception;
	/**
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	public List<ProductAccountMapper> findProductAccountMapper(ProductAccountMapper param) throws Exception;

	/**ProductAccountMapper--用于编辑
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void editProductAccountMapperBySecurityCode(String investorId, String securityCodeOld,String securityCodeNew) throws Exception;
	
}
