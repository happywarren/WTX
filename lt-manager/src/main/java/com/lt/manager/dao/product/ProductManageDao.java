package com.lt.manager.dao.product;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.bean.user.ProductAccountMapper;
import com.lt.model.product.Product;
import com.lt.vo.product.ProductVo;

/**
 * 商品管理dao
 * @author jingwb
 *
 */
public interface ProductManageDao {

	/**
	 * 创建商品
	 * @param product
	 */
	public void inserProduct(ProductParamVO param);
	
	/**
	 * 批量编辑商品
	 * @param product
	 */
	public void updateProducts(ProductParamVO param);
	
	/**
	 * 删除商品
	 * @param product
	 */
	public void deleteProduct(ProductParamVO param);
	
	/**
	 * 查询商品集--分页
	 * @param param
	 * @return
	 */
	public List<ProductVo> selectProductPage(ProductParamVO param);
	
	/**
	 * 查询商品数量
	 * @param param
	 * @return
	 */
	public Integer selectProductCount(ProductParamVO param);
	
	/**
	 * 返回商品代码集合
	 * @param param
	 * @return
	 */
	public List<String> selectProductCodeS(ProductParamVO param);
	
	/**
	 * 查询商品map
	 * 
	 * @param param
	 * @return
	 */
	public ProductVo selectProductMap(ProductParamVO param);
	
	/**
	 * 修改商品排序加一
	 */
	public void updateProductSort();
	
	/**
	 * 查询商品信息集合
	 * @param param
	 * @return
	 */
	public List<Product> selectProducts(ProductParamVO param);
	
	/**
	 * 查询商品实体信息
	 * @return
	 */
	public List<ProductVo> selectProductVos();
	
	/**
	 * 批量修改排序
	 * @param list
	 */
	public void updateProSortForList(List<Product> list);
	
	/**
	 * 查询商品基本信息返回一条数据
	 * @param param
	 * @return
	 */
	public Product selectProductOne(ProductParamVO param);
	
	/**
	 * 将排序由大改小
	 * @param map
	 */
	public void updateProSortForGTL(Map<String,Object> map);
	
	/**
	 * 将排序由小改大
	 * @param map
	 */
	public void updateProSortForLTG(Map<String,Object> map);
	
	/**
	 * 根据参数查询商品数量
	 * @param map
	 * @return
	 */
	public Integer selectProCountByParam(Map<String,String> map);
	
	
	/**
	 * 获取品种集合
	 * @return
	 */
	public List<Product> selectProShortCode(Product product);
	
	/**
	 *
	 * @param param
	 * @return
	 */
	public Integer selectProCountByCode(ProductParamVO param);
	
	/**
	 * 查询未完成的订单数
	 * @param param
	 * @return
	 */
	public Integer selectOrderCount(List<String> list);
	
	/**
	 * 查询未完成的积分订单数
	 * @param list
	 * @return
	 */
	public Integer selectScoreOrderCount(List<String> list);
	
	/**
	 * 根据币种获取汇率
	 * @param currency
	 * @return
	 */
	public String selectRateByCurrency(String currency);
	
	
	public void updateTimeSummer();
	
	public void updateTimeWinter();
	
	public List<ProductVo> selectProductList(ProductParamVO param);
	
	/**
	 * 查询该商品对应品种id在券商配置中的信息
	 * @param productCode
	 * @return
	 */
	public List<ProductAccountMapper> selectProAccMapperList(String productCode);

	public Product getLastProduct(Integer productId);
}
