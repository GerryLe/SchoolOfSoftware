package com.rosense.module.system.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 请假实体
 *
 */
/*@Entity
@Table(name = "simple_class")*/
public class LeaveEntity extends IdEntity {
	
	private String class_no;//班级编号
	private String class_name;//班级名称
	private String stu_no;//学号；
	private String stu_name;//姓名
	private String content;//请假原由
	private String apply_date;//申请日期
	private String apply_starDate;//申请开始时间
	private String apply_endDate;//申请结束时间
	private double apply_days;//申请天数
	
	private String leader;//辅导员姓名
	private int leaderApproval;//辅导员审批(0:未通过，1：通过，2：不通过)
	private String leaderRemark;//辅导员审批备注
	private int leaveStatement;//申请状态(0：暂未通过，1：通过：2：不通过，3：撤销) 
	public String getClass_no() {
		return class_no;
	}
	public void setClass_no(String class_no) {
		this.class_no = class_no;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getStu_no() {
		return stu_no;
	}
	public void setStu_no(String stu_no) {
		this.stu_no = stu_no;
	}
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getApply_date() {
		return apply_date;
	}
	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}
	public String getApply_starDate() {
		return apply_starDate;
	}
	public void setApply_starDate(String apply_starDate) {
		this.apply_starDate = apply_starDate;
	}
	public String getApply_endDate() {
		return apply_endDate;
	}
	public void setApply_endDate(String apply_endDate) {
		this.apply_endDate = apply_endDate;
	}
	public double getApply_days() {
		return apply_days;
	}
	public void setApply_days(double apply_days) {
		this.apply_days = apply_days;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public int getLeaderApproval() {
		return leaderApproval;
	}
	public void setLeaderApproval(int leaderApproval) {
		this.leaderApproval = leaderApproval;
	}
	public String getLeaderRemark() {
		return leaderRemark;
	}
	public void setLeaderRemark(String leaderRemark) {
		this.leaderRemark = leaderRemark;
	}
	public int getLeaveStatement() {
		return leaveStatement;
	}
	public void setLeaveStatement(int leaveStatement) {
		this.leaveStatement = leaveStatement;
	}
	
	
	
}
