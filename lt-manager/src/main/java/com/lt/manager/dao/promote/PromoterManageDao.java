package com.lt.manager.dao.promote;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.promote.PromoteParamVo;
import com.lt.manager.bean.user.UserBase;
import com.lt.model.promote.Promoter;
import com.lt.model.promote.PromoterUserMapper;
import com.lt.model.statistic.StatisticBrancherDayLog;
import com.lt.model.statistic.StatisticBrancherSummaryLog;

/**
 * 推广员dao
 * @author jingwb
 *
 */
public interface PromoterManageDao {

	/**
	 * 查询推广员数量
	 * @param promoter
	 * @return
	 */
	public Integer selectPromoterCount(Promoter promoter);
	
	/**
	 * 查询推广员列表--分页
	 * @param param
	 * @return
	 */
	public List<PromoteParamVo> selectPromoterPage(PromoteParamVo param);
	
	/**
	 * 查询推广员列表--数量
	 * @param param
	 * @return
	 */
	public Integer selectPromoterPageCount(PromoteParamVo param);
	
	/**
	 * 查询推广员数据列表--分页
	 * @param param
	 * @return
	 */
	public List<PromoteParamVo> selectPromoterDataPage(PromoteParamVo param);
	
	/**
	 * 查询下线列表--分页
	 * @param param
	 * @return
	 */
	public List<PromoteParamVo> selectBrancherPage(PromoteParamVo param);
	
	/**
	 * 查询下线列表数量
	 * @param param
	 * @return
	 */
	public Integer selectBrancherPageCount(PromoteParamVo param);
	
	/**
	 * 修改推广员信息
	 * @param promoter
	 */
	public void updatePromoter(Promoter promoter);
	
	/**
	 * 查询用户数
	 * @param user
	 * @return
	 */
	public String selectUserId(UserBase user);
	
	/**
	 * 修改关系
	 * @param mapper
	 */
	public void updatePromoterUserMapper(PromoterUserMapper mapper);
	
	/**
	 * 新增关系
	 * @param mapper
	 */
	public void insertPromoterUserMapper(PromoterUserMapper mapper);
	
	/**
	 * 查询推广员信息
	 * @param userId
	 * @return
	 */
	public Map<String,Object> selectPromoterInfo(String userId);
	
	/**
	 * 查询关系
	 * @param userId
	 * @return
	 */
	public Map<String,Object> selectPromoterUserMapper(String userId);
	
	/**
	 * 删除推广员
	 * @param userId
	 */
	public void deletePromoter(String userId);
	
	/**
	 * 修改下线汇总状态
	 * @param map
	 */
	public void updateBrancherSummaryStatus(Map<String,Object> map);
	
	public Integer selectPromoterUserMapperCount(PromoterUserMapper mapper);
	
	public Integer selectBrancherSummaryLogCount(StatisticBrancherSummaryLog log);
	
	public void initBrancherSummaryLog(StatisticBrancherSummaryLog log);
	
	public PromoterUserMapper selectFirstPromoterUserMapper(String userId);
	
	public Integer selectBrancherDayLogCount(StatisticBrancherDayLog log);
	
	public void insertBrancherDayLog(StatisticBrancherDayLog log);
}
