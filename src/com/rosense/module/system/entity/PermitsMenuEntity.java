package com.rosense.module.system.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 许可访问的菜单
 *
 */
@Entity
@Table(name = "simple_permits_menu")
public class PermitsMenuEntity extends IdEntity {
	
	/**
	 * 主体类型（USER、ROLE）
	 */
	private String principalType; //主体类型（USER、ROLE）
	
	/**
	 * 主体ID
	 */
	private String principalId; //主体ID
	
	/**
	 * 菜单操作资源的状态
	 */
	private String menuId; //菜单资源ID
	
	/**
	 * 菜单操作资源的状态
	 */
	private String menuName; //菜单资源名称
	
	/**
	 * 菜单操作资源的状态
	 */
	private String menuHref; //菜单资源的URL
	
	/**
	 * 菜单操作资源的状态
	 */
	private String menuPid; //菜单父节点
	
	/**
	 * 菜单操作资源的状态
	 */
	private Integer menuSort = new Integer(1); //菜单排序号
	
	/**
	 * 菜单操作资源的状态
	 */
	private String menuIconCls; //菜单图标
	
	/**
	 * 菜单操作资源的状态
	 */
	private String menuColor;//菜单颜色
	
	/**
	 * 菜单操作资源的状态
	 */
	private String state = "open"; //菜单状态
	
	/**
	 * 菜单操作资源的状态
	 */
	private String alias;//简称
	
	/**
	 * 菜单操作资源的状态
	 */
	private Integer selectOnly = new Integer(0);//只用于查询

	public void setSelectOnly(Integer selectOnly) {
		this.selectOnly = selectOnly;
	}

	public Integer getSelectOnly() {
		return selectOnly;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	private Set<PermitsOperEntity> permitsOper = new HashSet<PermitsOperEntity>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "permitsMenu")
	public Set<PermitsOperEntity> getPermitsOper() {
		return permitsOper;
	}

	public void setMenuColor(String menuColor) {
		this.menuColor = menuColor;
	}

	public String getMenuColor() {
		return menuColor;
	}

	public void setPermitsOper(Set<PermitsOperEntity> permitsOper) {
		this.permitsOper = permitsOper;
	}

	public String getPrincipalType() {
		return principalType;
	}

	public String getMenuIconCls() {
		return menuIconCls;
	}

	public void setMenuIconCls(String menuIconCls) {
		this.menuIconCls = menuIconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setPrincipalType(String principalType) {
		this.principalType = principalType;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getMenuId() {
		return menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public Integer getMenuSort() {
		return menuSort;
	}

	public void setMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
	}

	public String getMenuHref() {
		return menuHref;
	}

	public void setMenuHref(String menuHref) {
		this.menuHref = menuHref;
	}

	public String getMenuPid() {
		return menuPid;
	}

	public void setMenuPid(String menuPid) {
		this.menuPid = menuPid;
	}

	
	public String toString() {
		return "PermitsMenuEntity [principalType=" + principalType + ", principalId=" + principalId + ", menuId=" + menuId + ", menuHref=" + menuHref
				+ ", menuPid=" + menuPid + ", permitsOper=" + permitsOper + "]";
	}

}
