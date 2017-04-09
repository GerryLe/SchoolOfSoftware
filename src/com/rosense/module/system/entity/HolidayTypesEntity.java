package com.rosense.module.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

@Entity
@Table(name = "holidaytypes")
public class HolidayTypesEntity extends IdEntity{
	private String holidayname;//假期类型名称

	public String getHolidayname() {
		return holidayname;
	}

	public void setHolidayname(String holidayname) {
		this.holidayname = holidayname;
	}
	

}
