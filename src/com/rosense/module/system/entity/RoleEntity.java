package com.rosense.module.system.entity;

import java.sql.Clob;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.rosense.basic.dao.ExtFieldEntity;

/**
 * 角色实体
 *
 */
@Entity
@Table(name = "simple_role")
public class RoleEntity extends ExtFieldEntity {
	
	/**
	 * 角色名称
	 */
	private String name; //角色名称
	
	/**
	 * 角色序列号
	 */
	private String sn; //角色序列号
	
	/**
	 * 默认分配角色，1：默认角色
	 */
	private Integer defaultRole; //默认分配角色，1：默认角色
	
	/**
	 * 角色备注
	 */
	private Clob remark; //角色备注
	
	private Set<UserEntity> users = new HashSet<UserEntity>(0);

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "simple_user_roles", joinColumns = { @JoinColumn(name = "roleId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "userId", nullable = false, updatable = false) })
	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public Integer getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(Integer defaultRole) {
		this.defaultRole = defaultRole;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Clob getRemark() {
		return remark;
	}

	public void setRemark(Clob remark) {
		this.remark = remark;
	}

}
