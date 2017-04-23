package com.rosense.module.app.web.form;

import java.io.Serializable;
import java.util.Date;

import com.rosense.basic.model.PageHelper;

/**
 * 帖子回复
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public class BbsItemForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	private String bbsId;//帖子Id
	private String content;//发送内容
	private String userId;//发送者
	private Date createDate;//发送时间
	private int zans;//点赞
	private int floor;//楼层
	private boolean delete;
	private String userName;
	private int reffloor;//引用ID

	public int getReffloor() {
		return reffloor;
	}

	public void setReffloor(int reffloor) {
		this.reffloor = reffloor;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isDelete() {
		return delete;
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

	public String getBbsId() {
		return bbsId;
	}

	public void setBbsId(String bbsId) {
		this.bbsId = bbsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

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
