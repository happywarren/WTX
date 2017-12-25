package com.lt.business.core.dao.promote;

import java.util.List;

import com.lt.model.promote.Promoter;

/**
 * 推广员dao
 * @author jingwb
 *
 */
public interface IPromoterDao {

	/**
	 * 添加推广员信息
	 * @param vo
	 */
	public void insertPromoter(Promoter vo);
	
	/**
	 * 查询所有推广员信息
	 * @return
	 */
	public List<Promoter> selectPromoters();
	
	/**
	 * 查询推广员信息
	 * @param userId
	 * @return
	 */
	public Promoter selectPromoterByUserId(String userId);
	
	/**
	 * 获取推广员数量
	 * @param vo
	 * @return
	 */
	public Integer selectPromoterCount(Promoter vo);
	
	public void updatePromoter(Promoter vo);
}
