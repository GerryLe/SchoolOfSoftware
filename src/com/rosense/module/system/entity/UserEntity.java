package com.rosense.module.system.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 用户实体
 */
@Entity
@Table(name = "simple_user")
public class UserEntity extends IdEntity {
	
	/**
	 * 姓名
	 */
	private String name; //姓名
	
	/**
	 * 登陆账号
	 */
	private String account; //登陆账号
	
	
	
	/**
	 * 登陆密码
	 */
	private String password = ""; //登陆密码
	
	/**
	 * 账号状态（0：允许登录，1：禁止登录）
	 */
	private Integer status = new Integer(0); //账号状态（0：允许登录，1：禁止登录）
	
	/**
	 * 详细用户Id
	 */
	private String personId;//详细用户Id
	
	/**
	 * 权限角色
	 */
	private Set<RoleEntity> roles = new HashSet<RoleEntity>(0); //权限角色
	
	/**
	 * 最后登录时间
	 */
	private long lastLoginTime = 0;//最后登录时间
	
	/**
	 * 创建时间
	 */
	private Date created;//创建时间
	
	/**
	 * 假期Id
	 */
	private String holidaysId;//假期Id
	
	
	private String stu_no;//学号
	private String stu_name;//姓名
	
	private String tea_no;//编号
	private String tea_name;//姓名
	

	public String getTea_no() {
		return tea_no;
	}

	public void setTea_no(String tea_no) {
		this.tea_no = tea_no;
	}

	public String getTea_name() {
		return tea_name;
	}

	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}

	public UserEntity() {
		this.created = new Date();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return created;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "simple_user_roles", joinColumns = { @JoinColumn(name = "userId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "roleId", nullable = false, updatable = false) })
	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonId() {
		return personId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHolidaysId() {
		return holidaysId;
	}

	public void setHolidaysId(String holidaysId) {
		this.holidaysId = holidaysId;
	}

	public String getStu_no() {
		return stu_no;
	}

	public void setStu_no(String stu_no) {
		this.stu_no = stu_no;
	}

	
	public String getStu_name() {
		return stu_name;
	}

	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	

}
