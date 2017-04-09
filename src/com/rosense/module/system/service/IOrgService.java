package com.rosense.module.system.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.OrgForm;

public interface IOrgService {

	/**
	 * 添加组织机构
	 */
	public Msg add(OrgForm form);

	/**
	 * 删除组织机构
	 */
	public Msg delete(OrgForm form);

	/**
	 * 修改组织机构
	 */
	public Msg update(OrgForm form);

	/**
	 * 获取一个组织机构对象
	 */
	public OrgForm get(OrgForm form);

	/**
	 * 根据名称获取部门
	 */
	public OrgForm getByName(String name);

	/**
	 * 生成所有组织机构树
	 */
	public List<OrgForm> tree(String pid);
	
	/**
	 * 根据名字获取部门对象
	 */
	public OrgForm getId(String orgName);
	
	/**
	 * 根据id获取部门及用户
	 */
	public String userAndOrgtree(String pid);
	
	/**
	 * 根据名字获取部门对象
	 */
	public String getName(String id);

}
