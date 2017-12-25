package com.lt.util.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 邀请码生成工具
 * @author jingwb
 *
 */
public class InvitecodeTools {

	private static int PROMOTE_CODE_S = 102877;
	
	public static AtomicInteger count = new AtomicInteger(0);
	/**
	 * 
	 *	
	 * 描述:统计所有点击个数
	 * @since   v1.0.0 
	 * @return  void
	 */
	public static int increase() {  
		return count.incrementAndGet();
	}  
	
	/**
	 * 根据用户ID生成邀请码，同一用户生成的邀请码唯一
	 * @since   v1.0.0 
	 * @param userId
	 * @return  String
	 */
	public static String getInvitecodeByUserId(Integer userId) {
		int pro = PROMOTE_CODE_S + userId;
		return Integer.toHexString(pro);
	}

	/**
	 * 通过邀请码获取邀请用户的ID
	 * @since   v1.0.0 
	 * @param promoteCode
	 * @return
	 * @return  Integer
	 */
	public static Integer getUserIdByCode(String promoteCode) {
		Integer userId = null;
		if (StringTools.isNotBlank(promoteCode)) {
			try {
				userId = Integer.parseInt(promoteCode, 16) - PROMOTE_CODE_S;
			} catch (NumberFormatException e) {

			}
		}
		return userId;
	}
}
