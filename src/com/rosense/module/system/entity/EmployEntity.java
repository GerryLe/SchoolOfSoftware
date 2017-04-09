package com.rosense.module.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

@Entity
@Table(name = "simple_employ")
public class EmployEntity extends IdEntity{

	private String applyName; //申请人
	private String applyAreaAndorg;  //申请地点、部门
	private String applyPosition; //申请对外职位
	private String applyGenre; //申请公司内部级别
	private String applyPayment ;//申请薪金
	private String applySubsidy;//申请补贴
	private String applyNumber; //申请人数
	private String  planComeDate; //预计入职日期
	private String applyBudget; //申请预算
	private String reason;// 申请理由
	private String  duty;//主要负责工作
	private String  sex;//性别
	private String minAge;//最小年龄
	private String maxAge;//最大年龄
	private String degree;//学历
	private String profession;//专业
	private String workOld;//工作经验
	private String otherRequire;//其他要求
	private String orgCharge;//部门主管
	private String superior;//直接上司
	private String manager;//当地总经理
	private String boss;//总裁
	private String orgChargeDate;//部门主管审核日期
	private String superiorDate;//直接上司审核日期
	private String managerDate;//当地总经理审核日期
	private String bossDate;//总裁审核日期
	private String employName;//受聘雇员
	private String positionName;//职位
	private String methods;//招聘方法
	private String employComeDate;//到职日期
	private String  replaces;//替补
	private String adds;//新增 
	
	private int orgChargeApply;//部门主管审批(0:未通过，1：不通过，2：通过)
	private int superiorApply;//直接上司审批(0:未通过，1：不通过，2：通过)
	private int managerApply;// 当地总经理审批(0：未通过，1：通过：2不通过)
	private int bossApply;//当地总裁&集团HRA总监&CRO(0：未通过，1：通过：2不通过)
	private int applyStatement;//加班申请状态(0：暂未通过，1：通过：2不通过)
//	private int approval;//人事审批(0：不通过，1：通过)
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getApplyAreaAndorg() {
		return applyAreaAndorg;
	}
	public void setApplyAreaAndorg(String applyAreaAndorg) {
		this.applyAreaAndorg = applyAreaAndorg;
	}
	public String getApplyPosition() {
		return applyPosition;
	}
	public void setApplyPosition(String applyPosition) {
		this.applyPosition = applyPosition;
	}
	public String getApplyGenre() {
		return applyGenre;
	}
	public void setApplyGenre(String applyGenre) {
		this.applyGenre = applyGenre;
	}
	public String getApplyPayment() {
		return applyPayment;
	}
	public void setApplyPayment(String applyPayment) {
		this.applyPayment = applyPayment;
	}
	public String getApplySubsidy() {
		return applySubsidy;
	}
	public void setApplySubsidy(String applySubsidy) {
		this.applySubsidy = applySubsidy;
	}
	public String getApplyNumber() {
		return applyNumber;
	}
	public void setApplyNumber(String applyNumber) {
		this.applyNumber = applyNumber;
	}
	public String getPlanComeDate() {
		return planComeDate;
	}
	public void setPlanComeDate(String planComeDate) {
		this.planComeDate = planComeDate;
	}
	public String getApplyBudget() {
		return applyBudget;
	}
	public void setApplyBudget(String applyBudget) {
		this.applyBudget = applyBudget;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getMinAge() {
		return minAge;
	}
	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}
	public String getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getWorkOld() {
		return workOld;
	}
	public void setWorkOld(String workOld) {
		this.workOld = workOld;
	}
	public String getOtherRequire() {
		return otherRequire;
	}
	public void setOtherRequire(String otherRequire) {
		this.otherRequire = otherRequire;
	}
	public String getOrgCharge() {
		return orgCharge;
	}
	public void setOrgCharge(String orgCharge) {
		this.orgCharge = orgCharge;
	}
	public String getSuperior() {
		return superior;
	}
	public void setSuperior(String superior) {
		this.superior = superior;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getBoss() {
		return boss;
	}
	public void setBoss(String boss) {
		this.boss = boss;
	}
	public String getOrgChargeDate() {
		return orgChargeDate;
	}
	public void setOrgChargeDate(String orgChargeDate) {
		this.orgChargeDate = orgChargeDate;
	}
	public String getSuperiorDate() {
		return superiorDate;
	}
	public void setSuperiorDate(String superiorDate) {
		this.superiorDate = superiorDate;
	}
	public String getManagerDate() {
		return managerDate;
	}
	public void setManagerDate(String managerDate) {
		this.managerDate = managerDate;
	}
	public String getBossDate() {
		return bossDate;
	}
	public void setBossDate(String bossDate) {
		this.bossDate = bossDate;
	}
	public String getEmployName() {
		return employName;
	}
	public void setEmployName(String employName) {
		this.employName = employName;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getMethods() {
		return methods;
	}
	public void setMethods(String methods) {
		this.methods = methods;
	}
	public String getEmployComeDate() {
		return employComeDate;
	}
	public void setEmployComeDate(String employComeDate) {
		this.employComeDate = employComeDate;
	}
	
	public String getReplaces() {
		return replaces;
	}
	public void setReplaces(String replaces) {
		this.replaces = replaces;
	}
	public String getAdds() {
		return adds;
	}
	public void setAdds(String adds) {
		this.adds = adds;
	}
	public int getOrgChargeApply() {
		return orgChargeApply;
	}
	public void setOrgChargeApply(int orgChargeApply) {
		this.orgChargeApply = orgChargeApply;
	}
	public int getSuperiorApply() {
		return superiorApply;
	}
	public void setSuperiorApply(int superiorApply) {
		this.superiorApply = superiorApply;
	}
	public int getManagerApply() {
		return managerApply;
	}
	public void setManagerApply(int managerApply) {
		this.managerApply = managerApply;
	}
	public int getBossApply() {
		return bossApply;
	}
	public void setBossApply(int bossApply) {
		this.bossApply = bossApply;
	}
	public int getApplyStatement() {
		return applyStatement;
	}
	public void setApplyStatement(int applyStatement) {
		this.applyStatement = applyStatement;
	}
	
	
	
}
