package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.UserForm;

public interface IUserService {



	/**
	 * 获取一个用户对象
	 */
	public UserForm get(String id);


	/**
	 * 用户查询
	 */
	public DataGrid datagrid(UserForm form,String selectType,String searchKeyName);

	/**
	 * 用户帐号删除
	 * @param form
	 * @return
	 */
	public Msg delete(UserForm form);

	/**
	 * 重设密码
	 */
	public Msg resetPwd(String id);

	/**
	 * 修改密码
	 */
	public Msg updatePwd(UserForm form);

	/**
	 * 锁定账号
	 */
	public Msg lockUser(String id);

	/**
	 * 修改头像
	 */
	public Msg updatePhoto(String photo);
	
	public Msg batchUserRole(UserForm form);
	
}
