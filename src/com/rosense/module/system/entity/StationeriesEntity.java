package com.rosense.module.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;
/**
 * 文具列表，HR添加文具
 * @author admin
 *
 */
@Entity
@Table(name="stationeries_define")
public class StationeriesEntity extends IdEntity{
	private String stationeryName ;//文具名称
	private String stationeryType ;//文具型号
	private String stationeryUnit ;//文具单位
	private String remark ;//备注
	private Double price ;//单价
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
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
}
