package com.lt.business.core.dao.promote;

import java.util.List;

import com.lt.model.promote.PromoterLevel;

/**
 * 推广员等级dao
 * @author jingwb
 *
 */
public interface IPromoterLevelDao {

	/**
	 * 查询推广等级
	 * @param vo
	 * @return
	 */
	public List<PromoterLevel> selectPromoterLevels(PromoterLevel vo);
}
