package com.rosense.module.system.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 部门
 * @author Can-Dao
 * 	
 * 2015年8月19日 上午9:07:33
 *
 */
@Entity
@Table(name = "simple_org")
public class OrgEntity extends IdEntity {
	
	private String name; //部门
	private Integer sort = new Integer(1); //组织机构的排序号
	private String pid;//父ID
	private Date created;

	public OrgEntity() {
		this.created = new Date();
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return created;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}


}
