package com.rosense.module.system.service;

import java.util.List;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.UserForm;

public interface ITeacherService {

	/**
	 * 添加教师信息
	 */
	public Msg addTea(UserForm form);

	/**
	 * 删除教师信息
	 */
	public Msg delete(UserForm form);

	/**
	 * 修改教师信息
	 */
	public Msg update(UserForm form);

	/**
	 * 获取一个用户对象
	 */
	public UserForm get(String id);

	
	/**
	 * 查询员工
	 */
	public DataGrid datagridperson(UserForm form,String selectType,String searchKeyName);
	
	/**
	 * 用户登陆验证
	 */
	public LoginUser loginCheck(LoginUser form);


	/**
	 * 导入用户信息
	 */
	public Msg importFile(List<UserForm> importUserList);
	
	public int equlasValAccount(String account);


	/**
	 * 查看个人信息
	 */
	public DataGrid datagridpersonal();
	
}
