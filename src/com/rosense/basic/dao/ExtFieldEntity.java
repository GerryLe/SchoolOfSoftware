package com.rosense.basic.dao;

import java.util.Date;

import javax.persistence.MappedSuperclass;

/**
 * 
 * @author 黄家乐
 * 	
 *
 */
@MappedSuperclass
public class ExtFieldEntity extends IdEntity {
	private String userId; //创建者名称
	private Date created; //创建日期
	private Long version = new Long(0); //版本号
	private Integer is_delete = new Integer(0);;//是否删除
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Integer getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(Integer is_delete) {
		this.is_delete = is_delete;
	}

	
	

}
