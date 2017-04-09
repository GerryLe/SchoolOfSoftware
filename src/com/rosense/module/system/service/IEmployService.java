package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.EmployForm;

public interface IEmployService {

	/**
	 * 
	 * 添加
	 */
	 
	public Msg add(EmployForm form);
	
	
	/**
	 * 
	 * 更新
	 */
	
	public Msg update(EmployForm form);
	
	/**
	 * 
	 * 删除
	 */
	public Msg delete(EmployForm form);
	
	/**
	 * 
	 * 查询
	 */
	public DataGrid select(EmployForm form);
	
	/**
	 * 
	 * 加载信息
	 */
	public EmployForm get(String id);
	
	/**
	 * 
	 * 审核通过
	 */
	
	public Msg approvaltrue(String id);
	
	/**
	 * 
	 * 审核不通过
	 */
	
	public Msg approvalfalse(String id);
}
