package com.rosense.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

@Entity
@Table(name="stationery_apply")
public class StationeryEntity extends IdEntity{
	private String stationeryName ;//文具名称
	private String stationeryType ;//文具型号
	private int amount ;//数量
	private String stationeryUnit ;//文具单位
	private String remark ;//备注
	private String orderMonth ;//订购日，19号凌晨为分界点，凌晨前算本月，后算次月
	private Date submitDate ;//申请时间
	private String userId ;//用户id
	private String orgId ;//部门id
	private Double price ;//单价
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getOrderMonth() {
		return orderMonth;
	}
	public void setOrderMonth(String orderMonth) {
		this.orderMonth = orderMonth;
	}
	public String getStationeryName() {
		return stationeryName;
	}
	public void setStationeryName(String stationeryName) {
		this.stationeryName = stationeryName;
	}
	public String getStationeryType() {
		return stationeryType;
	}
	public void setStationeryType(String stationeryType) {
		this.stationeryType = stationeryType;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getStationeryUnit() {
		return stationeryUnit;
	}
	public void setStationeryUnit(String stationeryUnit) {
		this.stationeryUnit = stationeryUnit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
