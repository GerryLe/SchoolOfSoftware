package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.PageHelper;

public class MessageForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	private String content; //内容
	private String type;//类型
	private int readed = 0;//是否已读
	private String readedTxt;

	public void setType(String type) {
		this.type = type;
	}

	public void setReadedTxt(String readedTxt) {
		this.readedTxt = readedTxt;
	}

	public String getReadedTxt() {
		return readedTxt;
	}

	public String getType() {
		return type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setReaded(int readed) {
		this.readed = readed;
	}

	public int getReaded() {
		return readed;
	}

}
