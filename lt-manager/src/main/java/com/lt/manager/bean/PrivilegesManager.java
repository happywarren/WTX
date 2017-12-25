package com.lt.manager.bean;


import java.util.List;

import org.slf4j.Logger;

import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.PurView;
import com.lt.util.LoggerTools;
import com.lt.util.utils.StringTools;

/**
 * 
 * 说明：权限验证
 *
 */
public class PrivilegesManager {
	private static PrivilegesManager instans = new PrivilegesManager();
	private Logger logger = LoggerTools.getInstance(getClass());
	public static PrivilegesManager getInstance() {
		return instans;
	}


	public boolean authValid(int authId, PurView purView, String urlTpl) {
		urlTpl = urlTpl.trim();
		if (!StringTools.isNotEmpty(urlTpl)) {
			return false;
		}
		for (PurView p : purView.getSubPurView()) {
			if (p.getSubPurView().size() == 0) {
				if (urlTpl.equals(p.getAuth().getUrl().trim())) {
					return true;
				}
			} else {
				for (PurView s : p.getSubPurView()) {
					if (urlTpl.equals(s.getAuth().getUrl().trim())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 员工权限重组
	 * @param userAuthList  员工权限列表
	 * @return
	 */
	public PurView userAuthRecompose(List<Auth> userAuthList) {
		PurView userPurView = new PurView(-1, new Auth());
		List<PurView> userPurViewList = userPurView.getSubPurView();
		/* 将用户权限添加到对应的父级菜单下，此处未进行递归操作，只支持二级菜单  */
		for (Auth auth : userAuthList) {
			PurView t = new PurView(auth.getId(), auth);
			userPurViewList.add(t);
		}
		return userPurView;
	}
}
