package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.PageHelper;

public class AttendanceForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	private  String uid;//用户id
	private String class_id;//班级编号
	private String class_name;//班级名称
	private String semester;//学期
	private String school_year;//学年
	private String stu_no;//学号；
	private String stu_name;//姓名
	private String apply_date;//申请日期

	
	private Integer section1;//第一节(0:未选中，1：选中)
	private Integer section2;//第二节
	private Integer section3;//第三节
	private Integer section4;//第四节
	private Integer section5;//第五节
	private Integer section6;//第六节
	private Integer section7;//第七节
	private Integer section8;//第八节
	private Integer section9;//第九节
	private Integer section10;//第十节
	private Integer section11;//第十一节
	
	
	
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
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
	public Integer getSection1() {
		return section1;
	}
	public void setSection1(Integer section1) {
		this.section1 = section1;
	}
	public Integer getSection2() {
		return section2;
	}
	public void setSection2(Integer section2) {
		this.section2 = section2;
	}
	public Integer getSection3() {
		return section3;
	}
	public void setSection3(Integer section3) {
		this.section3 = section3;
	}
	public Integer getSection4() {
		return section4;
	}
	public void setSection4(Integer section4) {
		this.section4 = section4;
	}
	public Integer getSection5() {
		return section5;
	}
	public void setSection5(Integer section5) {
		this.section5 = section5;
	}
	public Integer getSection6() {
		return section6;
	}
	public void setSection6(Integer section6) {
		this.section6 = section6;
	}
	public Integer getSection7() {
		return section7;
	}
	public void setSection7(Integer section7) {
		this.section7 = section7;
	}
	public Integer getSection8() {
		return section8;
	}
	public void setSection8(Integer section8) {
		this.section8 = section8;
	}
	public Integer getSection9() {
		return section9;
	}
	public void setSection9(Integer section9) {
		this.section9 = section9;
	}
	public Integer getSection10() {
		return section10;
	}
	public void setSection10(Integer section10) {
		this.section10 = section10;
	}
	public Integer getSection11() {
		return section11;
	}
	public void setSection11(Integer section11) {
		this.section11 = section11;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}
