package com.lt.manager.bean.sys;


import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 说明：菜单数据结构
 * @author zheng_zhi_rui@163com
 * @date 2015年4月7日
 *
 */
public class PurView {

	private int id;
	private Auth auth;
	private List<PurView> subPurView = new ArrayList<PurView>();

	public PurView() {
		super();
	}

	public PurView(int id, Auth auth) {
		super();
		this.id = id;
		this.auth = auth;
	}

	public PurView(int id, Auth auth, List<PurView> subPurView) {
		super();
		this.id = id;
		this.auth = auth;
		this.subPurView = subPurView;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public List<PurView> getSubPurView() {
		return subPurView;
	}

	public void setSubPurView(List<PurView> subPurView) {
		this.subPurView = subPurView;
	}

}
