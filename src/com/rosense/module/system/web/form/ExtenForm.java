package com.rosense.module.system.web.form;

public class ExtenForm implements java.io.Serializable {

	/**
	 * id
	 */
	private int id;
	/**
	 * cid
	 */
	private int cid;
	/**
	 * 地区 020 广州   021 上海   010北京  00852 香港
	 */
	private String locateid;
	/**
	 * 分机号id
	 */
	private int callerid;
	/**
	 * 分机号
	 */
	private int exten;
	private String secret;
	private String callforward;
	private String didnum;
	private String ast;
	/**
	 * 可用状态      1在用  0停用
	 */
	private int active;
	/**
	 * 使用人
	 */
	private String remark;

	public ExtenForm() {

	}

	public ExtenForm(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getLocateid() {
		return locateid;
	}

	public void setLocateid(String locateid) {
		this.locateid = locateid;
	}

	public int getCallerid() {
		return callerid;
	}

	public void setCallerid(int callerid) {
		this.callerid = callerid;
	}

	public int getExten() {
		return exten;
	}

	public void setExten(int exten) {
		this.exten = exten;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getCallforward() {
		return callforward;
	}

	public void setCallforward(String callforward) {
		this.callforward = callforward;
	}

	public String getDidnum() {
		return didnum;
	}

	public void setDidnum(String didnum) {
		this.didnum = didnum;
	}

	public String getAst() {
		return ast;
	}

	public void setAst(String ast) {
		this.ast = ast;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
