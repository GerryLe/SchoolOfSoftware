package com.rosense.module.app.entity;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 帖子回复
* @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Table(name = "simple_bbsitem")
@Entity
public class BbsItemEntity {
	private String bbsId;//帖子Id
	private Clob content;//发送内容
	private String userId;//发送者
	private Date createDate;//发送时间
	private int zans;//点赞
	private int floor;//楼层
	private boolean deleted;
	private String userName;
	protected String id;
	private int reffloor;//引用ID

	public int getReffloor() {
		return reffloor;
	}

	public void setReffloor(int reffloor) {
		this.reffloor = reffloor;
	}

	@Id
	@Column(length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(length = 64)
	public String getUserName() {
		return userName;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getFloor() {
		return floor;
	}

	public void setZans(int zans) {
		this.zans = zans;
	}

	public int getZans() {
		return zans;
	}

	public BbsItemEntity() {
		this.createDate = new Date();
	}

	@Column(length = 64)
	public String getBbsId() {
		return bbsId;
	}

	public void setBbsId(String bbsId) {
		this.bbsId = bbsId;
	}

	public Clob getContent() {
		return content;
	}

	public void setContent(Clob content) {
		this.content = content;
	}

	@Column(length = 64)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
