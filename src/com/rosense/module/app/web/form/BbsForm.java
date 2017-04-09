package com.rosense.module.app.web.form;

import java.io.Serializable;
import java.util.Date;

import com.rosense.basic.model.PageHelper;

/**
 * @author Can-Dao
 * 	
 * 2015年8月24日 下午4:04:33
 *
 */
public class BbsForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;//标题
	private String content;//发送内容
	private String userId;//发送者
	private Date createDate;//发送时间
	private Date updateDate;
	private String userName;
	private int remarks;//评论
	private int views;//访问
	private int zans;//点赞
	private int floor;//楼层
	private boolean recommend;

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getUpdateDate() {
		return updateDate;
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

	public void setViews(int views) {
		this.views = views;
	}

	public int getViews() {
		return views;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setRemarks(int remarks) {
		this.remarks = remarks;
	}

	public int getRemarks() {
		return remarks;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
