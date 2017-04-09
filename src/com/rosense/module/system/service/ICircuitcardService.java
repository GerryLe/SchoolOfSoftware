package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.UserForm;

public interface ICircuitcardService {

	/**
	 * 添加信息
	 * @param form
	 * @return
	 */
	public Msg add(UserForm form);
	
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
	 * 编辑申请信息
	 * @param id
	 * @return
	 */
	public Msg update(UserForm form);
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
	 * 注销申请
	 */
	public Msg delete(UserForm form);
	
	/**
	 * 申请完成
	 */
	public Msg complete(UserForm form);
	
	/**
	 * 获取用户的角色默认值
	 */
	public int roleDefault();
}
