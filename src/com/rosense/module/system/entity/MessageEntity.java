package com.rosense.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.ExtFieldEntity;

/**
 * 消息管理
 */
@Entity
@Table(name = "simple_message")
public class MessageEntity extends ExtFieldEntity {
	private String content; //内容
	private int readed = 0; //审批，0未读，1已读
	private String type;//类型

	public MessageEntity() {
		setCreated(new Date());
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public int getReaded() {
		return readed;
	}

	public void setReaded(int readed) {
		this.readed = readed;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
