package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.EasyuiTree;

public class OrgForm extends EasyuiTree<OrgForm> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name; //部门名称
	private Integer sort; //部门的排序号

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
