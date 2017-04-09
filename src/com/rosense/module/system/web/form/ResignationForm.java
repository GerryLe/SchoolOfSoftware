package com.rosense.module.system.web.form;

import java.io.Serializable;
import java.util.Date;

import com.rosense.basic.model.PageHelper;

public class ResignationForm extends PageHelper implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean flag = false;
	private Integer state  = new Integer(0);
	private String id ;
	private boolean showExtre = false;//是否显示邮箱处理
	private boolean valid = true;
	
	private String chinaname;
	private String name;
	private Date employmentDate;
	private String childOrgName;
	private String parentOrgName;
	private String positionName;
	private String account;
	private String roleName;//角色名
	private Integer defaultRole;//角色号
	private Date applyDate;
	
	private Double workingAge;
	private String area;
	private String immediateBoss;
	private Date predictResignationDate;
	private String resignationReason;
	
	private String directAuditOpinion;
	private String emailDeal;
	private String directRemark;
	private String auditOpinion1;
	private String auditOpinion2;
	private String auditOpinion3;
	private String remark1;
	private String remark2;
	private String remark3;
	
	private String personId;//员工id
	private String positionId;//岗位id
	private String orgId;//部门id
	private String userId;//用户id
	private String roleId;//角色id
	private Integer sta = new Integer(0);
	public Integer getSta() {
		return sta;
	}
	public void setSta(Integer sta) {
		this.sta = sta;
	}
	private Integer sta1 = new Integer(0);
	private Integer sta2 = new Integer(0);
	private Integer sta3 = new Integer(0);
	public Integer getSta1() {
		return sta1;
	}
	public void setSta1(Integer sta1) {
		this.sta1 = sta1;
	}
	public boolean isShowExtre() {
		return showExtre;
	}
	public void setShowExtre(boolean showExtre) {
		this.showExtre = showExtre;
	}
	public Integer getSta2() {
		return sta2;
	}
	public void setSta2(Integer sta2) {
		this.sta2 = sta2;
	}
	public Integer getSta3() {
		return sta3;
	}
	public void setSta3(Integer sta3) {
		this.sta3 = sta3;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getChinaname() {
		return chinaname;
	}
	public void setChinaname(String chinaname) {
		this.chinaname = chinaname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getEmploymentDate() {
		return employmentDate;
	}
	public String getRoleName() {
		return roleName;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}
	public String getChildOrgName() {
		return childOrgName;
	}
	public void setChildOrgName(String childOrgName) {
		this.childOrgName = childOrgName;
	}
	public String getParentOrgName() {
		return parentOrgName;
	}
	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public Integer getDefaultRole() {
		return defaultRole;
	}
	public void setDefaultRole(Integer defaultRole) {
		this.defaultRole = defaultRole;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Double getWorkingAge() {
		return workingAge;
	}
	public void setWorkingAge(Double workingAge) {
		this.workingAge = workingAge;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getImmediateBoss() {
		return immediateBoss;
	}
	public void setImmediateBoss(String immediateBoss) {
		this.immediateBoss = immediateBoss;
	}
	public Date getPredictResignationDate() {
		return predictResignationDate;
	}
	public void setPredictResignationDate(Date predictResignationDate) {
		this.predictResignationDate = predictResignationDate;
	}
	public String getResignationReason() {
		return resignationReason;
	}
	public void setResignationReason(String resignationReason) {
		this.resignationReason = resignationReason;
	}
	public String getDirectAuditOpinion() {
		return directAuditOpinion;
	}
	public void setDirectAuditOpinion(String directAuditOpinion) {
		this.directAuditOpinion = directAuditOpinion;
	}
	public String getEmailDeal() {
		return emailDeal;
	}
	public void setEmailDeal(String emailDeal) {
		this.emailDeal = emailDeal;
	}
	public String getDirectRemark() {
		return directRemark;
	}
	public void setDirectRemark(String directRemark) {
		this.directRemark = directRemark;
	}
	public String getAuditOpinion1() {
		return auditOpinion1;
	}
	public void setAuditOpinion1(String auditOpinion1) {
		this.auditOpinion1 = auditOpinion1;
	}
	public String getAuditOpinion2() {
		return auditOpinion2;
	}
	public void setAuditOpinion2(String auditOpinion2) {
		this.auditOpinion2 = auditOpinion2;
	}
	public String getAuditOpinion3() {
		return auditOpinion3;
	}
	public void setAuditOpinion3(String auditOpinion3) {
		this.auditOpinion3 = auditOpinion3;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	public boolean isFlag() {
		return flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
