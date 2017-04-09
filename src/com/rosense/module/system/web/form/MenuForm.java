package com.rosense.module.system.web.form;

import java.io.Serializable;
import java.util.List;

import com.rosense.basic.model.EasyuiTree;

public class MenuForm extends EasyuiTree<MenuForm> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String menuId;//菜单唯一值
	private String name; //菜单名称
	private String href; //链接地址							
	private String type; //菜单类型(菜单类别：T，菜单：M，操作：O)
	private String iconCls; //菜单图标	
	private String color;//颜色
	private Integer sort;
	private Integer isShow = new Integer(1);
	private String state = "open";
	private String remark;
	private boolean icon = true;
	private List<MenuForm> opers;
	private String alias;//简称

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	public void setIcon(boolean icon) {
		this.icon = icon;
	}

	public boolean isIcon() {
		return icon;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	
	public String getText() {
		return name;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuId() {
		return menuId;
	}

	public List<MenuForm> getOpers() {
		return opers;
	}

	public void setOpers(List<MenuForm> opers) {
		this.opers = opers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public String toString() {
		return "MenuForm [name=" + name + ", opers=" + opers + "]";
	}

}
