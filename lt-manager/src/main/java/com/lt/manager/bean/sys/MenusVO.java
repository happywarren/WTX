package com.lt.manager.bean.sys;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSONObject;

public class MenusVO extends Menus {

	/**
	 * Description: 
	 */
	private static final long serialVersionUID = 9102918695601457315L;

	private List<MenusVO> children; //子菜单

	private List<AuthVO> auths; //菜单关联权限

	private Boolean checked;

	public List getChildren() {
		if((children==null||children.isEmpty())&&auths!=null){
			return auths;
		}
		return children;
	}

	public void setChildren(List<MenusVO> children) {
		this.children = children;
	}

	public void setChildrenOfMenu(List<Menus> children2) {
		if (children == null) {
			children = new ArrayList<>();
		}
		for (Menus menus : children2) {
			MenusVO child = new MenusVO();
			BeanUtils.copyProperties(menus, child);
			children.add(child);
		}
	}
	
	public void setChildrenOfMenuById(List<Menus> children2,Integer menuId) {
		if (children == null) {
			children = new ArrayList<>();
		}
		for (Menus menus : children2) {
			MenusVO child = new MenusVO();
			BeanUtils.copyProperties(menus, child);
			if(child.getId().intValue() == menuId.intValue()){
				child.setChecked(true);
			}
			children.add(child);
		}
	}

	public void addChild(MenusVO vo) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(vo);
	}

	/*public List<AuthVO> getAuths() {
		return auths;
	}*/

	public void setAuths(List<AuthVO> auths) {
		this.auths = auths;
	}

	public void setAuthsByOrg(List<Auth> auths) {
		if (auths == null) {
			auths = new ArrayList<>();
		}
		for (Auth auth : auths) {
			try {
				AuthVO child = new AuthVO();
				BeanUtils.copyProperties(auth, child);
				addAuth(child);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(this.auths);
				System.out.println(JSONObject.toJSON(auth));
			}
			
		}
	}

	public void addAuth(AuthVO authVO) {
		if (auths == null) {
			auths = new ArrayList<>();
		}
		auths.add(authVO);
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
