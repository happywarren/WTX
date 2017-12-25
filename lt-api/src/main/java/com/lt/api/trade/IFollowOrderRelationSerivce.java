package com.lt.api.trade;

import java.util.List;

import com.lt.vo.trade.FollowerVo;
import com.lt.vo.trade.OrderVo;
import com.lt.vo.trade.ProfitAndLossUserVo;
/**
 * 
 * @author sky
 *
 */
public interface IFollowOrderRelationSerivce {
	/**
	 * 保存原单与跟单的关系，结构为map<原单号,List<跟单号>>
	 */
	public void saveOrigToFollowOrderRelation(String key,List<OrderVo> followOrders);
	/**
	 * 根据原单号查询对应的跟单号列表
	 * @return
	 */
	public List<OrderVo> queryFollowOrdersByOrig(String origNum);
	/**
	 * 根据原单号删除跟单号
	 * @param origNum
	 */
	public void deleteFollowOrdersByOrig(String origNum);
	/**
	 * 查询长盈长亏列表
	 * @return
	 */
	public List<ProfitAndLossUserVo> queryFollowedNameList();
	/**
	 * 查询 跟单列表名单
	 * @return
	 */
	public  List<FollowerVo> queryFollowerList();
}
