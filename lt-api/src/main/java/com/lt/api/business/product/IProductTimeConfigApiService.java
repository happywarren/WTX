package com.lt.api.business.product;

import java.util.List;

import com.lt.model.product.ProductTimeConfig;
import com.lt.vo.defer.ProductDeferTimeInfoVo;

/**
 * 业务模块 商品时间配置API接口
 * @author guodw
 *
 */
public interface IProductTimeConfigApiService {

	/**
	 * 查询商品时间配置
	 * @param productId
	 * @return
	 */
	public List<ProductTimeConfig> findProductTimeConfigByProductId(Integer productId);

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
	 * 获取系统清仓时间列表
	 * @return
	 * @return:       List<ProductDeferTimeInfo>
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年5月5日 上午6:16:40
	 */
	public List<ProductDeferTimeInfoVo> queryAllProductClearTime();
}
