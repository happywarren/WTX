package com.lt.business.core.mongo.promote;

import com.lt.business.core.vo.PromoteLibrary;

public interface IPromoteMongoDao {
	/**
	 * 统计次数
	 * @param count
	 * @param userId
	 */
	void visitCount(Integer count,String userId);
	
	/**
	 * 将推广员id，ip，手机号加入库中
	 * @param userId
	 * @param ip
	 * @param tele
	 */
	public void addPromoteLibrary(String userId, String ip, String tele);
	
	/**
	 * 获取点击次数
	 * @param userId
	 * @return
	 */
	public int getVisitCount(String userId);
	
	/**
	 * 获取推广员信息通过手机号
	 * @param tele
	 * @return
	 */
	public PromoteLibrary getPromoteLibraryByTele(String tele);
	/**
	 * 获取推广员信息通过ip
	 * @param tele
	 * @return
	 */
	public PromoteLibrary getPromoteLibraryByIP(String ip);
}
