package com.lt.business.core.dao.product;

import java.util.List;

import com.lt.model.product.ProductTimeConfig;
import com.lt.vo.defer.ProductDeferTimeInfoVo;

public interface IProductTimeConfigDao {

	/**
	 * 根据商品ID 查询相关商品配置信息
	 * @param productId
	 * @return
	 */
	List<ProductTimeConfig> findProductTimeConfigByProductId(Integer productId);
	
	/**
	 * 根据商品ID 查询相关商品配置信息(冬令时)
	 * @param productId
	 * @return
	 */
	List<ProductTimeConfig> findProductTimeConfigWinterByProductId(Integer productId);

	/**
	 * 根据ID查询结算配置
	 * @param id
	 * @return
	 * @return:       SettleTypeInfo
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年3月16日 下午3:40:12
	 */
	public List<ProductDeferTimeInfoVo> queryAllProductDeferTime();

	/**
	 * 根据ID查询冬令时结算配置
	 * @return
	 */
	public List<ProductDeferTimeInfoVo> queryAllProductDeferTimeWinter();

	/**
	 * 获取系统清仓时间列表
	 * @return
	 * @return:       List<ProductDeferTimeInfo>
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年5月5日 上午6:16:40
	 */
	public List<ProductDeferTimeInfoVo> queryAllProductClearTime();

    /**
     * 获取系统清仓时间列表
     * @return
     * @return:       List<ProductDeferTimeInfo>
     * @throws
     * @author        yuanxin
     * @Date          2017年5月5日 上午6:16:40
     */
    public List<ProductDeferTimeInfoVo> queryAllProductClearTimeWinter();
}
