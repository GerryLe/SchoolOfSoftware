package com.rosense.module.system.web.form;

import java.io.Serializable;
import java.util.Date;

import com.rosense.basic.model.PageHelper;

public class HolidaysUserForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;

	private String useraccount;//员工编号
	private String grade; //行政级别
	private String name;//姓名
	private String positionname;//部门名称
	private String workAge; //工龄
	private String month;//当前月份
	private Date employmentDate;//入职日期
	private String becomeStaffDate; //转正日期
	private double shouldHoliday;//全年应有假期
	private double alreadyAnnualLeave;//本年度已休年假
	private double shouldhaveannualleave;//应有年假
	private double theremainingannualleave;//剩余年假
	private double lastyearsremainingSiLingfalse;//上年度剩余司龄假
	private double thisyearshouldbeSiLingfalse;//本年度应有司龄假
	private double alreadySiLingFalse;//本年度已休司龄假
	private double theremainingSiLingfalse;//剩余司龄假
	private double residueHoliday;//本年度剩余假期天数
	private double thismonthhasbeenonmedicalleave;//本月以休病假
	private double lastyearsremainingpaidleave;//上年度剩余调休
	private double thisyearsdaysworkovertime;//本年度加班天数
	private double theremainingpaidleave;//剩余调休
	private double notvalidonanannualbasis;//本年度累计事假
	private double haveToTakePaidLeave;//本年度已休补休
	
	
	
	
	private double annualLeave;//后台计算年假
	private int calculateLength;//计算工龄
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public double getThismonthhasbeenonmedicalleave() {
		return thismonthhasbeenonmedicalleave;
	}
	public void setThismonthhasbeenonmedicalleave(double thismonthhasbeenonmedicalleave) {
		this.thismonthhasbeenonmedicalleave = thismonthhasbeenonmedicalleave;
	}
	public double getNotvalidonanannualbasis() {
		return notvalidonanannualbasis;
	}
	public void setNotvalidonanannualbasis(double notvalidonanannualbasis) {
		this.notvalidonanannualbasis = notvalidonanannualbasis;
	}
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}
	public double getShouldhaveannualleave() {
		return shouldhaveannualleave;
	}
	public void setShouldhaveannualleave(double shouldhaveannualleave) {
		this.shouldhaveannualleave = shouldhaveannualleave;
	}
	public double getTheremainingannualleave() {
		return theremainingannualleave;
	}
	public void setTheremainingannualleave(double theremainingannualleave) {
		this.theremainingannualleave = theremainingannualleave;
	}
	public double getLastyearsremainingSiLingfalse() {
		return lastyearsremainingSiLingfalse;
	}
	public void setLastyearsremainingSiLingfalse(double lastyearsremainingSiLingfalse) {
		this.lastyearsremainingSiLingfalse = lastyearsremainingSiLingfalse;
	}
	public double getThisyearshouldbeSiLingfalse() {
		return thisyearshouldbeSiLingfalse;
	}
	public void setThisyearshouldbeSiLingfalse(double thisyearshouldbeSiLingfalse) {
		this.thisyearshouldbeSiLingfalse = thisyearshouldbeSiLingfalse;
	}
	public double getTheremainingSiLingfalse() {
		return theremainingSiLingfalse;
	}
	public void setTheremainingSiLingfalse(double theremainingSiLingfalse) {
		this.theremainingSiLingfalse = theremainingSiLingfalse;
	}
	public double getLastyearsremainingpaidleave() {
		return lastyearsremainingpaidleave;
	}
	public void setLastyearsremainingpaidleave(double lastyearsremainingpaidleave) {
		this.lastyearsremainingpaidleave = lastyearsremainingpaidleave;
	}
	public double getThisyearsdaysworkovertime() {
		return thisyearsdaysworkovertime;
	}
	public void setThisyearsdaysworkovertime(double thisyearsdaysworkovertime) {
		this.thisyearsdaysworkovertime = thisyearsdaysworkovertime;
	}
	public double getTheremainingpaidleave() {
		return theremainingpaidleave;
	}
	public void setTheremainingpaidleave(double theremainingpaidleave) {
		this.theremainingpaidleave = theremainingpaidleave;
	}
	public Date getEmploymentDate() {
		return employmentDate;
	}
	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}
	public String getBecomeStaffDate() {
		return becomeStaffDate;
	}
	public void setBecomeStaffDate(String becomeStaffDate) {
		this.becomeStaffDate = becomeStaffDate;
	}
	public double getShouldHoliday() {
		return shouldHoliday;
	}
	public void setShouldHoliday(double shouldHoliday) {
		this.shouldHoliday = shouldHoliday;
	}
	public double getResidueHoliday() {
		return residueHoliday;
	}
	public void setResidueHoliday(double residueHoliday) {
		this.residueHoliday = residueHoliday;
	}
	public double getAlreadyAnnualLeave() {
		return alreadyAnnualLeave;
	}
	public void setAlreadyAnnualLeave(double alreadyAnnualLeave) {
		this.alreadyAnnualLeave = alreadyAnnualLeave;
	}
	public double getAlreadySiLingFalse() {
		return alreadySiLingFalse;
	}
	public void setAlreadySiLingFalse(double alreadySiLingFalse) {
		this.alreadySiLingFalse = alreadySiLingFalse;
	}
	public double getAnnualLeave() {
		return annualLeave;
	}
	public void setAnnualLeave(double annualLeave) {
		this.annualLeave = annualLeave;
	}
	public int getCalculateLength() {
		return calculateLength;
	}
	public void setCalculateLength(int calculateLength) {
		this.calculateLength = calculateLength;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getPositionname() {
		return positionname;
	}
	public void setPositionname(String positionname) {
		this.positionname = positionname;
	}
	public String getWorkAge() {
		return workAge;
	}
	public void setWorkAge(String workAge) {
		this.workAge = workAge;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public double getHaveToTakePaidLeave() {
		return haveToTakePaidLeave;
	}
	public void setHaveToTakePaidLeave(double haveToTakePaidLeave) {
		this.haveToTakePaidLeave = haveToTakePaidLeave;
	}


	

}
