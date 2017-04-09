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

import com.rosense.basic.dao.IdEntity;

@Entity
@Table(name = "simple_procedure")
public class ProcedureEntity extends IdEntity{

	private String menuId;//菜单唯一值
	private String name; //流程名称
	private String href; //链接地址							
	private String type; //流程类型类型(流程类别：T，菜单：M，操作：O)
	private String state = "open";
	private Clob remark;
	private ProcedureEntity menu;
	private Integer sort = new Integer(1);
	private Integer isShow = new Integer(1);
	private String alias;//简称
	private Set<ProcedureEntity> menus = new HashSet<ProcedureEntity>(0);
	
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
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
	
	@ManyToOne
	@JoinColumn(name = "pid")
	public ProcedureEntity getMenu() {
		return menu;
	}
	public void setMenu(ProcedureEntity menu) {
		this.menu = menu;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
	@OrderBy("sort asc")
	public Set<ProcedureEntity> getMenus() {
		return menus;
	}
	public void setMenus(Set<ProcedureEntity> menus) {
		this.menus = menus;
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
	
	
	
	
	
}
