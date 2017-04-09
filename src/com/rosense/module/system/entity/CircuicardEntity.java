package com.rosense.module.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

@Entity
@Table(name="simple_circuitcard")
public class CircuicardEntity extends IdEntity {

    private String name;//英文名
    private String chinaname;//中文名
    private String sex;//性别
    private String phone;//手机号码
	private String telePhone;//座机号码
	private int applyStatus;//申请状态（0：申请中，1：完成，2：撤销）
	private String applyDate;//申请时间
	private int orgChargeApprove;//主管审批（0：未审批，1：已通过，2：不通过）
	private String orgChargeResult;//主管审批结果
	private String orgId;//部门Id
	private String positionId;//职位Id
	private String email;//邮箱
	private String orgChargeName;//部门主管
	private String orgChargeRemark;//主管审批备注
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChinaname() {
		return chinaname;
	}
	public void setChinaname(String chinaname) {
		this.chinaname = chinaname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	public int getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(int applyStatus) {
		this.applyStatus = applyStatus;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public int getOrgChargeApprove() {
		return orgChargeApprove;
	}
	public void setOrgChargeApprove(int orgChargeApprove) {
		this.orgChargeApprove = orgChargeApprove;
	}
	public String getOrgChargeResult() {
		return orgChargeResult;
	}
	public void setOrgChargeResult(String orgChargeResult) {
		this.orgChargeResult = orgChargeResult;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrgChargeName() {
		return orgChargeName;
	}
	public void setOrgChargeName(String orgChargeName) {
		this.orgChargeName = orgChargeName;
	}
	public String getOrgChargeRemark() {
		return orgChargeRemark;
	}
	public void setOrgChargeRemark(String orgChargeRemark) {
		this.orgChargeRemark = orgChargeRemark;
	}
	
	
}
