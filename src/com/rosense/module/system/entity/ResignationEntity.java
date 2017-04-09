package com.rosense.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;
/**
 * 离职申请表
 * @author admin
 *
 */
@Entity
@Table(name="resignation")
public class ResignationEntity extends IdEntity{
	private boolean flag = false;//是否主管以上级别,主管以上级别需要may,ravic进行审核
	private Integer state = new Integer(0) ;//-1 不通过,0 部门负责人，1 shadow, 2 may, 3 ravic
	private boolean valid = true;//是否有效，false为失效离职申请的记录
	
	private String chinaname;//中文名称
	private String name;//英文名称
	private Date employmentDate;//入职时间
	private String childOrgName;//team,即所在部门
	private String parentOrgName;//部门，上级一部门
	private String positionName;//岗位
	private String account;//员工编号
	private Date applyDate;//申请时间
	
	
	private Double workingAge;//工龄
	private String area;//地区
	private String immediateBoss;//直属上级
	private Date predictResignationDate;//预计离职日期
	private String resignationReason;//离职理由
	
	private String directAuditOpinion;//上级审批意见
	private String emailDeal;//邮箱处理
	private String directRemark;//上级备注
	private String auditOpinion1;//shadow
	private String auditOpinion2;//may
	private String auditOpinion3;//ravic
	private String remark1;//shadow
	private String remark2;//may
	private String remark3;//ravic
	private String personId;//员工id
	private String userId;//用户id
	private Integer sta1 = new Integer(0);// 0 未审核 1通过 2不通过
	private Integer sta2 = new Integer(0);// 0 未审核 1通过 2不通过
	private Integer sta3 = new Integer(0);// 0 未审核 1通过 2不通过
	private Integer sta = new Integer(0);// 0 未审核 1通过 2不通过
	
	public Integer getSta1() {
		return sta1;
	}
	public void setSta1(Integer sta1) {
		this.sta1 = sta1;
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
	public Integer getSta() {
		return sta;
	}
	public void setSta(Integer sta) {
		this.sta = sta;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getEmploymentDate() {
		return employmentDate;
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
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
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
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
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

}

