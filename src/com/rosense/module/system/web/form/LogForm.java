package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.PageHelper;

public class LogForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title; //操作标题
	private String ip; //用户登录IP
	private String detail; //操作明细

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
