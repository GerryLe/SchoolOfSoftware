package com.rosense.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.ExtFieldEntity;

/**
 * 用户登录日志
 */
@Entity
@Table(name = "simple_log_login")
public class LoginLogEntity extends ExtFieldEntity {
	private String ip; //IP地址
	private String browserType; //浏览器类型
	private String browserVersion; //浏览器版本
	private String platformType; //平台类型
	private String detail; //详细信息

	public LoginLogEntity() {
		setCreated(new Date());
	}
	
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
