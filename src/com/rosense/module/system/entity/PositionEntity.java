package com.rosense.module.system.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 岗位实体
 *
 */
@Entity
@Table(name = "simple_position")
public class PositionEntity extends IdEntity {
	
	/**
	 * 岗位名称
	 */
	private String name; //岗位名称
	private String workType;//上班方式
	private String approveAuth;//审批权限（1：可审批，0：不可审批）
	/**
	 * 岗位集合
	 */
	private Set<PositionEntity> positions = new HashSet<PositionEntity>(0);
	private PositionEntity position;
	private Integer sort = new Integer(-1);
	private Date created;

	public PositionEntity() {
		this.created = new Date();
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return created;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return sort;
	}

	@OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
	public Set<PositionEntity> getPositions() {
		return positions;
	}

	public void setPositions(Set<PositionEntity> positions) {
		this.positions = positions;
	}

	@ManyToOne
	@JoinColumn(name = "pid")
	public PositionEntity getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getApproveAuth() {
		return approveAuth;
	}

	public void setApproveAuth(String approveAuth) {
		this.approveAuth = approveAuth;
	}

	
}
