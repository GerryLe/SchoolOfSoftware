package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.EasyuiTree;

public class ClassForm extends EasyuiTree<ClassForm> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String class_no;//班级编号
	private String class_name;//班级名称
	private String grade;//年级
	private String relation_profession;//所属专业
	private String tea_no;//班主任编号
	private String tea_name;//班主任姓名
	private int sum_stu;//学生人数
	private int maxNum;//最大人数
	private Integer sort; //班级排序号
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
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getRelation_profession() {
		return relation_profession;
	}
	public void setRelation_profession(String relation_profession) {
		this.relation_profession = relation_profession;
	}
	public String getTea_no() {
		return tea_no;
	}
	public void setTea_no(String tea_no) {
		this.tea_no = tea_no;
	}
	public String getTea_name() {
		return tea_name;
	}
	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}
	public int getSum_stu() {
		return sum_stu;
	}
	public void setSum_stu(int sum_stu) {
		this.sum_stu = sum_stu;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
}
