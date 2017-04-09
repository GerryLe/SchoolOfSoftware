package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.UserForm;

public interface ICompleteProbationService {

	/**
	 * 
	 * 查询
	 */
	public DataGrid select(UserForm form);
	
	/**
	 * 
	 * 加载信息
	 */
	public UserForm get(String id);
	
	/**
	 * 
	 * 审核通过
	 */
	
	public Msg approvaltrue(UserForm form);
	
	/**
	 * 
	 * 审核不通过
	 */
	
	public Msg approvalfalse(UserForm form);
	
	/**
	 * 通知审核
	 * @param id
	 * @return
	 */
	public Msg approvalnotice(String id) ;
	
	/**
	 * 获取当前用户角色编号
	 */
	public int getCurrentUserDefaultRole();
}
