package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.TransferForm;

public interface ITransferService {

	/**
	 * 添加信息
	 * @param form
	 * @return
	 */
	public Msg add(TransferForm form);
	
	/**
	 * 
	 * 查询
	 */
	public DataGrid select(TransferForm form);
	
	/**
	 * 
	 * 加载信息
	 */
	public TransferForm get(String id);
	
	/**
	 * 编辑申请信息
	 * @param id
	 * @return
	 */
	public Msg update(TransferForm form);
	/**
	 * 
	 * 审核通过
	 */
	
	public Msg approvaltrue(TransferForm form);
	
	/**
	 * 
	 * 审核不通过
	 */
	
	public Msg approvalfalse(TransferForm form);
	
	/**
	 * 通知审核
	 * @param id
	 * @return
	 */
	public Msg approvalnotice(String id) ;
	
	
	/**
	 * 注销申请
	 */
	public Msg delete(TransferForm form);
	
	/**
	 * 获取当前用户角色编号
	 */
	public int getCurrentUserDefaultRole();
}
