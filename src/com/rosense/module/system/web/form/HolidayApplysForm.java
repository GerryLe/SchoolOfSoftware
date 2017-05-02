package com.rosense.module.system.web.form;

import java.io.Serializable;
import java.util.Date;

import com.rosense.basic.model.PageHelper;

public class HolidayApplysForm extends PageHelper implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String uid;//用户id
	private String positionname;//部门名称
	private String chinaname;//中文名
	private String holiapplyUserName;// 假期申请人英文名
	private String holiapplyName;// 假期申请类型
	private String holiapplyContent;// 假期申请事由
	private String  holiapplyDays;// 假期申请天数
	private String holiapplyStartDate;// 假期开始日期
	private String  holiapplyEndDate;// 假期结束日期
	private String holiapplydirectorsopinion;//部门主管意见
	private int holiapplydirectorsapproval;//部门主管审批(0:未通过，1：不通过，2：通过)
	private String holiapplyhropinion;//人事意见
	private int holiapplyhrapproval;//人事审批(0:未通过，1：不通过，2：通过)
	private int holiapplystatement;//假期状态 (0:未通过，1：不通过，2：通过)
	private String holiapplyremark;//备注
	private String englishName;
	private String startHours;//开始小时
	private String endHours;//结束小时
	private String enclosure;//附件\
	private String enclosuretwo;//附件2
	private String applyForTime;//申请时间
	private String director;//主管姓名
	private String approvalTime;//主管审批时间
	private String  defaultRole;//角色
	private String pr;//参数
	private String class_name;//班级名称
    private String account;//学号
    
	
	public String getPr() {
		return pr;
	}
	public void setPr(String pr) {
		this.pr = pr;
	}
	public String getHoliapplyUserName() {
		return holiapplyUserName;
	}
	public void setHoliapplyUserName(String holiapplyUserName) {
		this.holiapplyUserName = holiapplyUserName;
	}
	public String getHoliapplyName() {
		return holiapplyName;
	}
	public void setHoliapplyName(String holiapplyName) {
		this.holiapplyName = holiapplyName;
	}
	public String getHoliapplyContent() {
		return holiapplyContent;
	}
	public void setHoliapplyContent(String holiapplyContent) {
		this.holiapplyContent = holiapplyContent;
	}
	public String getHoliapplyDays() {
		return holiapplyDays;
	}
	public void setHoliapplyDays(String holiapplyDays) {
		this.holiapplyDays = holiapplyDays;
	}
	public String getHoliapplyStartDate() {
		return holiapplyStartDate;
	}
	public void setHoliapplyStartDate(String holiapplyStartDate) {
		this.holiapplyStartDate = holiapplyStartDate;
	}
	public String getHoliapplyEndDate() {
		return holiapplyEndDate;
	}
	public void setHoliapplyEndDate(String holiapplyEndDate) {
		this.holiapplyEndDate = holiapplyEndDate;
	}
	public String getHoliapplydirectorsopinion() {
		return holiapplydirectorsopinion;
	}
	public void setHoliapplydirectorsopinion(String holiapplydirectorsopinion) {
		this.holiapplydirectorsopinion = holiapplydirectorsopinion;
	}
	public int getHoliapplydirectorsapproval() {
		return holiapplydirectorsapproval;
	}
	public void setHoliapplydirectorsapproval(int holiapplydirectorsapproval) {
		this.holiapplydirectorsapproval = holiapplydirectorsapproval;
	}
	public String getHoliapplyhropinion() {
		return holiapplyhropinion;
	}
	public void setHoliapplyhropinion(String holiapplyhropinion) {
		this.holiapplyhropinion = holiapplyhropinion;
	}
	public int getHoliapplyhrapproval() {
		return holiapplyhrapproval;
	}
	public void setHoliapplyhrapproval(int holiapplyhrapproval) {
		this.holiapplyhrapproval = holiapplyhrapproval;
	}
	
	public String getHoliapplyremark() {
		return holiapplyremark;
	}
	public void setHoliapplyremark(String holiapplyremark) {
		this.holiapplyremark = holiapplyremark;
	}
	public int getHoliapplystatement() {
		return holiapplystatement;
	}
	public void setHoliapplystatement(int holiapplystatement) {
		this.holiapplystatement = holiapplystatement;
	}
	public String getChinaname() {
		return chinaname;
	}
	public void setChinaname(String chinaname) {
		this.chinaname = chinaname;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPositionname() {
		return positionname;
	}
	public void setPositionname(String positionname) {
		this.positionname = positionname;
	}
	public String getStartHours() {
		return startHours;
	}
	public void setStartHours(String startHours) {
		this.startHours = startHours;
	}
	public String getEndHours() {
		return endHours;
	}
	public void setEndHours(String endHours) {
		this.endHours = endHours;
	}
	public String getEnclosure() {
		return enclosure;
	}
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}
	public String getApplyForTime() {
		return applyForTime;
	}
	public void setApplyForTime(String applyForTime) {
		this.applyForTime = applyForTime;
	}
	public String getEnclosuretwo() {
		return enclosuretwo;
	}
	public void setEnclosuretwo(String enclosuretwo) {
		this.enclosuretwo = enclosuretwo;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getApprovalTime() {
		return approvalTime;
	}
	public void setApprovalTime(String approvalTime) {
		this.approvalTime = approvalTime;
	}
	public String getDefaultRole() {
		return defaultRole;
	}
	public void setDefaultRole(String defaultRole) {
		this.defaultRole = defaultRole;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

}
