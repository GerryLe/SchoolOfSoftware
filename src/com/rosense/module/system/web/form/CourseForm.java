package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.PageHelper;

public class CourseForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String course_no;//课程编号
	private String course_name;//课程名称
	private double credit;//学分
	private String createTime;//创建时间
	public String getCourse_no() {
		return course_no;
	}
	public void setCourse_no(String course_no) {
		this.course_no = course_no;
	}
	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
