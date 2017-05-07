package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.PageHelper;

public class ScoreForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	private  String uid;//用户id
	private String class_id;//班级编号
	private String class_name;//班级名称
	private String semester;//学期
	private String school_year;//学年
	private String stu_no;//学号；
	private String stu_name;//姓名
	private String apply_date;//申请日期
    private String uuid;
    private String score;//分数
	private String credit;//学分
	private String course_id;//课程id
	private String course_name;//课程名称
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getSchool_year() {
		return school_year;
	}
	public void setSchool_year(String school_year) {
		this.school_year = school_year;
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
	public String getApply_date() {
		return apply_date;
	}
	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
	
	
}
