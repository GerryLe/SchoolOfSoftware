package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.PageHelper;

public class TransferForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String account;//帐号
	private String chinaname;//中文名
	private String name;//英文名
	private String phone;//手机号码
	private String sex;//性别
	private String age;//年龄
	private String area;//地区
	private String degree;//学历
	private String orgId;//原部门id
	private String orgName;//原部门名称
	private String orgChildId;//原部门2id
	private String orgChildName;//原部门2名称
	private String positionId;//原岗位id
	private String positionName;//原岗位名称
	private String positionEng;//原岗位英文名
	private String newOrgId;//新部门id
	private String newOrgName;//新部门名称
	private String newOrgChildId;//新部门2id
	private String newOrgChildName;//新部门2名称
	private String newPositionId;//新岗位id
	private String newPositionName;//新岗位名称
	private String newPositionEng;//新岗位英文名
	private String newOrgChargeName;//新部门主管
	private String newOrgChargeId;//新部门主管ID
	private String newOrgHeadName;//新部门负责人
	private String newOrgHeadResult;//新部门负责人审批结果
	private String newOrgHeadRemark;//新部门负责人审批备注
	private String transferMayName;//May
	private String transferMayResult;//May审批结果
	private String transferMayRemark;//May审批备注
	private String transferRavicName;//Ravic
	private String transferRavicResult;//Ravic审批结果
	private String transferRavicRemark;//Ravic审批备注
	private String role_ids; //角色Ids
    private String applyRemark;//备注信息
    private String transApplyDate;//申请日期
	private int orgHeadApprove;//直接上司审批(0:未通过，1：通过，2：不通过)
    private int orgMayApprove;//May审批(0:未通过，1：通过，2：不通过)
    private int orgRavicApprove;//Ravic审批(0:未通过，1：通过，2：不通过)
    private int transApplyStatus;//申请状态(0:申请中，1：申请成功，2：撤销)
    
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgChildId() {
		return orgChildId;
	}
	public void setOrgChildId(String orgChildId) {
		this.orgChildId = orgChildId;
	}
	public String getOrgChildName() {
		return orgChildName;
	}
	public void setOrgChildName(String orgChildName) {
		this.orgChildName = orgChildName;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getPositionEng() {
		return positionEng;
	}
	public void setPositionEng(String positionEng) {
		this.positionEng = positionEng;
	}
	public String getNewOrgId() {
		return newOrgId;
	}
	public void setNewOrgId(String newOrgId) {
		this.newOrgId = newOrgId;
	}
	public String getNewOrgName() {
		return newOrgName;
	}
	public void setNewOrgName(String newOrgName) {
		this.newOrgName = newOrgName;
	}
	public String getNewOrgChildId() {
		return newOrgChildId;
	}
	public void setNewOrgChildId(String newOrgChildId) {
		this.newOrgChildId = newOrgChildId;
	}
	public String getNewOrgChildName() {
		return newOrgChildName;
	}
	public void setNewOrgChildName(String newOrgChildName) {
		this.newOrgChildName = newOrgChildName;
	}
	public String getNewPositionId() {
		return newPositionId;
	}
	public void setNewPositionId(String newPositionId) {
		this.newPositionId = newPositionId;
	}
	public String getNewPositionName() {
		return newPositionName;
	}
	public void setNewPositionName(String newPositionName) {
		this.newPositionName = newPositionName;
	}
	public String getNewPositionEng() {
		return newPositionEng;
	}
	public void setNewPositionEng(String newPositionEng) {
		this.newPositionEng = newPositionEng;
	}
	public String getNewOrgChargeName() {
		return newOrgChargeName;
	}
	public void setNewOrgChargeName(String newOrgChargeName) {
		this.newOrgChargeName = newOrgChargeName;
	}
	public String getNewOrgHeadName() {
		return newOrgHeadName;
	}
	public void setNewOrgHeadName(String newOrgHeadName) {
		this.newOrgHeadName = newOrgHeadName;
	}
	public String getNewOrgHeadResult() {
		return newOrgHeadResult;
	}
	public void setNewOrgHeadResult(String newOrgHeadResult) {
		this.newOrgHeadResult = newOrgHeadResult;
	}
	public String getNewOrgHeadRemark() {
		return newOrgHeadRemark;
	}
	public void setNewOrgHeadRemark(String newOrgHeadRemark) {
		this.newOrgHeadRemark = newOrgHeadRemark;
	}
	public String getTransferMayName() {
		return transferMayName;
	}
	public void setTransferMayName(String transferMayName) {
		this.transferMayName = transferMayName;
	}
	public String getTransferMayResult() {
		return transferMayResult;
	}
	public void setTransferMayResult(String transferMayResult) {
		this.transferMayResult = transferMayResult;
	}
	public String getTransferMayRemark() {
		return transferMayRemark;
	}
	public void setTransferMayRemark(String transferMayRemark) {
		this.transferMayRemark = transferMayRemark;
	}
	public String getTransferRavicName() {
		return transferRavicName;
	}
	public void setTransferRavicName(String transferRavicName) {
		this.transferRavicName = transferRavicName;
	}
	public String getTransferRavicResult() {
		return transferRavicResult;
	}
	public void setTransferRavicResult(String transferRavicResult) {
		this.transferRavicResult = transferRavicResult;
	}
	public String getTransferRavicRemark() {
		return transferRavicRemark;
	}
	public void setTransferRavicRemark(String transferRavicRemark) {
		this.transferRavicRemark = transferRavicRemark;
	}
	public String getRole_ids() {
		return role_ids;
	}
	public void setRole_ids(String role_ids) {
		this.role_ids = role_ids;
	}
	public String getApplyRemark() {
		return applyRemark;
	}
	public void setApplyRemark(String applyRemark) {
		this.applyRemark = applyRemark;
	}
	public String getTransApplyDate() {
		return transApplyDate;
	}
	public void setTransApplyDate(String transApplyDate) {
		this.transApplyDate = transApplyDate;
	}
	public String getNewOrgChargeId() {
		return newOrgChargeId;
	}
	public void setNewOrgChargeId(String newOrgChargeId) {
		this.newOrgChargeId = newOrgChargeId;
	}
	public int getOrgHeadApprove() {
		return orgHeadApprove;
	}
	public void setOrgHeadApprove(int orgHeadApprove) {
		this.orgHeadApprove = orgHeadApprove;
	}
	public int getOrgMayApprove() {
		return orgMayApprove;
	}
	public void setOrgMayApprove(int orgMayApprove) {
		this.orgMayApprove = orgMayApprove;
	}
	public int getOrgRavicApprove() {
		return orgRavicApprove;
	}
	public void setOrgRavicApprove(int orgRavicApprove) {
		this.orgRavicApprove = orgRavicApprove;
	}
	public int getTransApplyStatus() {
		return transApplyStatus;
	}
	public void setTransApplyStatus(int transApplyStatus) {
		this.transApplyStatus = transApplyStatus;
	}
	
	
	
	
}
