package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.EasyuiTree;

public class PositionForm extends EasyuiTree<PositionForm> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name; //岗位名称
	private String workType;//上班方式（1：正常上班，0：弹性上班）
	private String approveAuth;//审批权限（1：可审批，0：不可审批）
	private int sort;

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getSort() {
		return sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getApproveAuth() {
		return approveAuth;
	}

	public void setApproveAuth(String approveAuth) {
		this.approveAuth = approveAuth;
	}

	
}
