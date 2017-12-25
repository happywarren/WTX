package com.lt.manager.service.product;


import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ProductParamVO;
import com.lt.model.product.Product;
import com.lt.util.utils.model.Response;
import com.lt.vo.product.ProductVo;

/**
 * 商品管理service
 * @author jingwb
 *
 */
public interface IProductManageService {

	/**
	 * 商品添加
	 * @param Product
	 * @return
	 * @throws Exception
	 */
	public void addProduct(ProductParamVO param) throws Exception;
	
	/**
	 * 编辑商品
	 * @param Product
	 * @return
	 * @throws Exception
	 */
	public void editProduct(ProductParamVO param) throws Exception;
	
	public boolean editOrAddProduct(ProductParamVO param) throws Exception;
	
	/**
	 * 修改商品基本信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean editProductInfo(ProductParamVO param) throws Exception;
	
	/**
	 * 修改商品排序
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editProductSort(ProductParamVO param) throws Exception;
	
	/**
	 * 置顶商品
	 * @param param
	 * @throws Exception
	 */
	public void topProducts(ProductParamVO param) throws Exception;
	
	/**
	 * 获取商品信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryProductDetail(ProductParamVO param) throws Exception;
	
	/**
	 * 批量删除商品
	 * @param Product
	 * @return
	 * @throws Exception
	 */
	public void removeProducts(ProductParamVO param) throws Exception;

	/**
	 * 获取商品集--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<Product> queryProductPage(ProductParamVO param) throws Exception;
	
	
	public List<ProductVo> queryProductList(ProductParamVO param) throws Exception;
	
	
	/**
	 * 获取商品数量
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer queryProductCount(ProductParamVO param) throws Exception;
	
	/**
	 * 从数据库中加载商品放到缓存中
	 * @throws Exception
	 */
	public void loadProAndTimeToRedis() throws Exception;
	
	/**
	 * 加载商品到redis中
	 * @throws Exception
	 */
	public void loadProductToRedis() throws Exception;
	
	/**
	 * 获取品种信息
	 * @return
	 */
	public List<Product> getProShortCode(Product product) throws Exception;

	/**
	 * 根据商品获取汇率
	 * @param string
	 * @return
	 */
	public String selectRate(String string);

	/**
	 * modify by 梅传颂
	 * 根据商品id获取商品信息
	 */
	public Product getProductByProductId(Integer productId);

	/**
	 * 重新加载清仓时间
	 */
	public void reloadProdClearTime(String ...proudctCodes);
	
	
	public void updateTimeSummer();
	
	public void updateTimeWinter();

	/**
	 * 推送差价合约点差变化消息给java行情服务器
	 */
	void pushSpreadModifyMsg(List<String> productList);
	
	/**
	 * 执行初始化平仓商品、券商数据
	 * @param userId
	 */
	void initInvestorFeeConfig(String userId);
}
