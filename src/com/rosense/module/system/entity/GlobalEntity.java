package com.rosense.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 *全局变量
 */
@Entity
@Table(name = "simple_global")
public class GlobalEntity extends IdEntity {
	private String gCode;//唯一标示
	private String gValue;//值
	private Date updateDate;//更新时间

	public GlobalEntity() {
		this.updateDate = new Date();
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public String getgCode() {
		return gCode;
	}

	public void setgCode(String gCode) {
		this.gCode = gCode;
	}

	public String getgValue() {
		return gValue;
	}

	public void setgValue(String gValue) {
		this.gValue = gValue;
	}

}
