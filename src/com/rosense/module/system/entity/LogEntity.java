package com.rosense.module.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.ExtFieldEntity;

/**
 * 用户操作日志
 */
@Entity
@Table(name = "simple_log")
public class LogEntity extends ExtFieldEntity {
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
