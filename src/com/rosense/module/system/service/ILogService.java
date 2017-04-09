package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.LogForm;
import com.rosense.module.system.web.form.LoginLogForm;

public interface ILogService {

	/**
	 * 添加用户操作日志
	 */

	public void add(String title, String detail);

	/**
	 * 删除用户操作日志
	 */
	public Msg delete(LogForm form);

	/**
	 * 用户操作日志列表查询
	 */
	public DataGrid datagrid(LogForm form);

	/****************************************************************************/
	/**
	 * 添加用户登录日志
	 */
	public Msg addLL(LoginLogForm form);

	/**
	 * 删除用户登录日志
	 */
	public Msg deleteLL(LoginLogForm form);

	/**
	 * 获取一个用户登录日志对象
	 */
	public LoginLogForm getLL(LoginLogForm form);

	/**
	 * 用户登录日志列表查询
	 */
	public DataGrid datagridLL(LoginLogForm form);


}
