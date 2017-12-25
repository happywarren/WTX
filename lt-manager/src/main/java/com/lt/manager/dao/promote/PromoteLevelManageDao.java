package com.lt.manager.dao.promote;

import java.util.List;

import com.lt.model.promote.PromoterLevel;

/**
 * 推广员级别dao
 * @author jingwb
 *
 */
public interface PromoteLevelManageDao {

	/**
	 * 新增推广员等级
	 * @param level
	 */
	public void insertPromoterLevel(String level);
	
	/**
	 * 获取推广等级信息
	 * @param level
	 * @return
	 */
	public PromoterLevel selectPromoterLevelOne(String level);
	
	/**
	 * 删除推广等级
	 * @param id
	 */
	public void deletePromoteLevel(Integer id);
	
	/**
	 * 查询推广等级
	 * @return
	 */
	public List<PromoterLevel> selectPromoterLevels();
}
