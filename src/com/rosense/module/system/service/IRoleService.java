package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.web.form.RoleForm;

public interface IRoleService {

	/**
	 * 添加角色
	 */
	public Msg add(RoleForm form);

	/**
	 * 删除角色
	 */
	public Msg delete(RoleForm form);

	/**
	 * 修改角色
	 */
	public Msg update(RoleForm form);

	/**
		 * 获取一个角色对象
		 */
	public RoleForm getByName(String name);

	/**
	 * 获取一个角色对象
	 */
	public RoleForm get(RoleForm form);

	/**
	 * 角色列表查询
	 * @param form
	 * @return
	 */
	public DataGrid datagrid(RoleForm form);

	/**
	 * 获取角色的资源
	 */
	public RoleForm getRoleMenus(RoleForm form);

	/**
	 * 角色数据初始化
	 */
	public void init(RoleEntity entity);

	
	/**
	 * 根据名称获取角色的ID值
	 * 
	 */
	public String getRoleId(String name);
}
