package com.rosense.module.system.web.form;

import java.io.Serializable;
import java.sql.Clob;
import java.util.List;

import com.rosense.basic.model.EasyuiTree;

public class ProcedureForm extends EasyuiTree<ProcedureForm> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String menuId;//菜单唯一值
	private String name; //流程名称
	private String href; //链接地址							
	private String type; //流程类型类型(流程类别：T，菜单：M，操作：O)
	private String state = "open";
	private Clob remark;//备注
	private String alias;//简称
	private List<ProcedureForm> opers;
	private Integer isShow = new Integer(1);
	
	
	
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




	public String getAlias() {
		return alias;
	}




	public void setAlias(String alias) {
		this.alias = alias;
	}




	public List<ProcedureForm> getOpers() {
		return opers;
	}




	public void setOpers(List<ProcedureForm> opers) {
		this.opers = opers;
	}

	public String toString() {
		return "ProcedureForm [name=" + name + ", opers=" + opers + "]";
	}


	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	
	
}
