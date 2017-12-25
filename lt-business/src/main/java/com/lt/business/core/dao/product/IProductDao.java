package com.lt.business.core.dao.product;

import java.util.List;
import java.util.Map;

import com.lt.vo.defer.PeroidOrderHolidayVo;
import com.lt.vo.defer.ProNextTradePeriodVo;
import org.apache.ibatis.annotations.Param;

import com.lt.model.product.Product;
import com.lt.util.error.LTException;
import com.lt.vo.product.ProductVo;


public interface IProductDao {

	/**
	 * 查询所有的商品数据
	 * @return
	 */
	List<ProductVo> findAllToProductVo()throws LTException;
	
	/**
	 * 查询商品 根据status
	 * @param status
	 * @return
	 * @throws LTException
	 */
	List<Product> findAllByStatus(int status)throws LTException;

	/**
	 * 更新商品市场状态
	 * @param marketStatus
	 */
	void updateProductStatus(@Param("marketStatus")Integer marketStatus,@Param("productName")String productName)throws LTException;

	/**
	 * 更新市场状态
	 * @param product
	 */
	void updateProduct(ProductVo product)throws LTException;
	/**
	 * 查询商品大厅
	 * @param map
	 * @return
	 */
	public List<ProductVo> selectProductLobby(Map<String,Object> map)throws LTException;
	
	/**
	 * 根据商品code查询商品相关信息
	 * @param proCode
	 * @return
	 */
	public ProductVo selectProductInfo(String proCode)throws LTException;
	
	
	/**
	 * 获取当前时间处于假日的所有商品code集合
	 * @param time 格式为2016-12-12 12:00:00
	 * @return
	 */
	public List<String> selectCodeByNow(String time)throws LTException;
	
	/**
	 * 查询当前日期处于假日的商品code
	 * @param time 格式为2016-12-12
	 * @return
	 */
	public List<Map<String,Object>> selectCodeAndTimeForHoliday(String time)throws LTException;
	
	/**
	 * 查询商品市场状态
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> selectMarketStatus(List<String> list);
	
	/**
	 * 查询连续三天都放假的数量
	 * @param map
	 * @return
	 */
	public Integer selectHolidayCount(Map<String,String> map);
	
	/**
	 * 根据币种获取汇率
	 * @param currency
	 * @return
	 */
	public String selectRateByCurrency(String currency);

	/**
	 * 根据productID查询 ProductVo
	 * @param productId
	 * @return
	 */
	public ProductVo selectProductInfoById(Integer productId);
	/**
	 * 加载合约基本信息
	 * @param id
	 * @return
	 */
	public Product loadProduct(Integer id);

	/**
	 * 查询相关品种的未开始的节假日
	 * @param list
	 * @return
	 * @return:       List<PeroidOrderHoliday>
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年2月8日 下午7:38:34
	 */
	public List<PeroidOrderHolidayVo> findAllCodeHoliday(List<String> list);

	/**
	 * 查询品种的清仓时间
	 * @param list
	 * @return
	 * @return:       List<ProNextTradePeriod>
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年2月9日 上午10:23:51
	 */
	public List<ProNextTradePeriodVo> qryNextDayTradeTime(List<String> list);
	
	/**
	 * 
	 * @param productTypeCode 产品类型编码
	 * @return
	 */
	public Integer getProductCountByCondition(@Param("investorId")String investorId,@Param("productTypeCode")String productTypeCode);

}
