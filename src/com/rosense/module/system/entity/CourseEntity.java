package com.rosense.module.system.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 课程实体
 *
 */
@Entity
@Table(name = "simple_course")
public class CourseEntity extends IdEntity {
	
	private String course_no;//课程编号
	private String course_name;//课程名称
	private double credit;//学分
	private String createTime;//创建时间
	private Integer status = new Integer(0);
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
