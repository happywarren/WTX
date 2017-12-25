package com.lt.trade.settle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.model.settle.SettleTmpBean;
import com.lt.model.settle.SettleTypeInfo;

/**   
* 项目名称：lt-trade   
* 类名称：SettleDao   
* 类描述：结算查询类   
* 创建人：yuanxin   
* 创建时间：2017年3月16日 下午3:15:39      
*/
public interface SettleDao {
	
	/**
	 * 根据ID查询结算配置
	 * @param id
	 * @return    
	 * @return:       SettleTypeInfo    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月16日 下午3:40:12
	 */
	public SettleTypeInfo querySettleCfgById(Integer id);
	
	/**
	 * 插入到结算临时表中
	 * @param list    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月16日 下午6:22:53
	 */
	public void insertSettleTmp(@Param("list")List<SettleTmpBean> list);
}
