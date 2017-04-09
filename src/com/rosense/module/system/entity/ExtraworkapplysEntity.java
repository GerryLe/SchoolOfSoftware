package com.rosense.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

@Entity
@Table(name = "extraworkapplys")
public class ExtraworkapplysEntity extends IdEntity {
	
	private String userid;//用户id
	private String extworkapplyname;//加班申请人
	private String extworkapplycontent;//加班申请事由
	private String extworkapplydays;//加班申请天数
	private Date extworkapplystartdata;//加班开始日期
	private Date extworkapplyenddata;//加班结束日期
	private int extworkdirectorsapproval;//部门主管审批(0:未通过，1：通过，2：不通过)
	private int extworkhrapproval;//人事审批(0:未通过，1：通过，2：不通过)
	private int extworkapplystatement;//加班申请状态(0：暂未通过，1：通过：2不通过)
	private String extworkremark;//备注
	
	
	public String getExtworkapplyname() {
		return extworkapplyname;
	}
	public void setExtworkapplyname(String extworkapplyname) {
		this.extworkapplyname = extworkapplyname;
	}
	public String getExtworkapplycontent() {
		return extworkapplycontent;
	}
	public void setExtworkapplycontent(String extworkapplycontent) {
		this.extworkapplycontent = extworkapplycontent;
	}
	public String getExtworkapplydays() {
		return extworkapplydays;
	}
	public void setExtworkapplydays(String extworkapplydays) {
		this.extworkapplydays = extworkapplydays;
	}
	public Date getExtworkapplystartdata() {
		return extworkapplystartdata;
	}
	public void setExtworkapplystartdata(Date extworkapplystartdata) {
		this.extworkapplystartdata = extworkapplystartdata;
	}
	public Date getExtworkapplyenddata() {
		return extworkapplyenddata;
	}
	public void setExtworkapplyenddata(Date extworkapplyenddata) {
		this.extworkapplyenddata = extworkapplyenddata;
	}

	public int getExtworkdirectorsapproval() {
		return extworkdirectorsapproval;
	}
	public void setExtworkdirectorsapproval(int extworkdirectorsapproval) {
		this.extworkdirectorsapproval = extworkdirectorsapproval;
	}

	public int getExtworkhrapproval() {
		return extworkhrapproval;
	}
	public void setExtworkhrapproval(int extworkhrapproval) {
		this.extworkhrapproval = extworkhrapproval;
	}
	public int getExtworkapplystatement() {
		return extworkapplystatement;
	}
	public void setExtworkapplystatement(int extworkapplystatement) {
		this.extworkapplystatement = extworkapplystatement;
	}
	public String getExtworkremark() {
		return extworkremark;
	}
	public void setExtworkremark(String extworkremark) {
		this.extworkremark = extworkremark;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	

}
