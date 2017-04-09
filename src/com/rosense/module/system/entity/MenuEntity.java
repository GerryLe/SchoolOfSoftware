package com.rosense.module.system.entity;

import java.sql.Clob;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.rosense.basic.dao.ExtFieldEntity;

@Entity
@Table(name = "simple_menu")
public class MenuEntity extends ExtFieldEntity {
	private String menuId;//菜单唯一值
	private String name; //菜单名称
	private String href; //链接地址							
	private String type; //菜单类型(菜单类别：T，菜单：M，操作：O)
	private String iconCls; //菜单图标	
	private String color;//颜色
	private Integer sort = new Integer(1);
	private Integer isShow = new Integer(1);
	private String state = "open";
	private Clob remark;
	private MenuEntity menu;
	private String alias;//简称

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	private Set<MenuEntity> menus = new HashSet<MenuEntity>(0);

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuId() {
		return menuId;
	}

	@ManyToOne
	@JoinColumn(name = "pid")
	public MenuEntity getMenu() {
		return menu;
	}

	public void setMenu(MenuEntity menu) {
		this.menu = menu;
	}

	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
	@OrderBy("sort asc")
	public Set<MenuEntity> getMenus() {
		return menus;
	}

	public void setMenus(Set<MenuEntity> menus) {
		this.menus = menus;
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

	public Clob getRemark() {
		return remark;
	}

	public void setRemark(Clob remark) {
		this.remark = remark;
	}

}
