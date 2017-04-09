package com.rosense.basic.model;


/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月18日 下午8:15:11
 *
 */
public class Msg {

	/** 是否成功 */
	private boolean status = false;

	/** 提示信息 */
	private String msg = "";

	/** 其他信息 */
	private Object obj = null;
	private String param1;
	private String param2;
	private String param3;

	public Msg() {
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public Msg(boolean status) {
		this.status = status;
	}

	public Msg(boolean status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public Msg(boolean status, String msg, Object obj) {
		this.status = status;
		this.msg = msg;
		this.obj = obj;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
