package com.lt.promote.dao.promote;

import java.util.List;
import java.util.Map;

import com.lt.model.promote.PromoterUserMapper;

/**
 * 推广员与用户关系dao
 * @author jingwb
 *
 */
public interface IPromoterUserMapperDao {

	/**
	 * 添加推广员与用户关系
	 * @param vo
	 */
	public void insertPromoterUserMapper(PromoterUserMapper vo);
	
	/**
	 * 查询所有依然存在关系的所有下线信息
	 * @return
	 */
	public List<PromoterUserMapper> selectPromoterUserMapper();
	
	/**
	 * 查询推广员的下线人员
	 * @return
	 */
	public List<PromoterUserMapper> selectPromoterBranchersByTime(String statisticTime);
	
	
	/**
	 * 查询推广员的下线人员
	 * @return
	 */
	public List<PromoterUserMapper> selectPromoterBranchers();
	
	/**
	 * 查询该用户的所有下线信息
	 * @param userId
	 * @return
	 */
	public List<PromoterUserMapper> selectPromoterUserMapperByUserId(String userId);
	
	/**
	 * 查询未生成下线汇总数据的下线
	 * @param userId
	 * @return
	 */
	public List<PromoterUserMapper> selectNoSummaryBranchers(String userId);
	
	/**
	 * 查询未生成当天下线日报的下线
	 * @param userId
	 * @return
	 */
	public List<PromoterUserMapper> selectNoDayLogBranchers(String userId);
	
	/**
	 * 查询一层下线数
	 * @param userId
	 * @return
	 */
	public Integer selectFirstPromoterCount(String userId);
	
	/**
	 * 查询二层下线数
	 * @param userId
	 * @return
	 */
	public Integer selectSecondPromoterCount(String userId);
	
	/**
	 * 判断该下线的上线是否为推广员
	 * @param userId
	 * @return
	 */
	public Integer selectPromoterCount(String userId);
	
	/**
	 * 判断该用户该上线是否关联过
	 * @param map
	 * @return
	 */
	public Integer selectIsRegister(Map<String,String> map);
}
