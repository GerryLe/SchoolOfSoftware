package com.rosense.module.system.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 成绩实体
 *
 */
@Entity
@Table(name = "simple_score")
public class ScoreEntity extends IdEntity {
	
	
	//private String stu_no;//学生编号
	private String course_id;//课程id
	private String class_id;//班级id
	private String score;//分数
	private String credit;//学分
	private String semester;//学期
	private String school_year;//学年
	private String apply_date;//录入日期
	private String uid;//用户id
	
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
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
	public String getApply_date() {
		return apply_date;
	}
	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}
