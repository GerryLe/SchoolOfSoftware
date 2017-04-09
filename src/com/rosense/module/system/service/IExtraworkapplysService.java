package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.ExtraworkapplysForm;

public interface IExtraworkapplysService {
	/**
	 * 添加
	 */
	public Msg add(ExtraworkapplysForm form);
	
	/**
	 * 删除
	 */
	public Msg delete(ExtraworkapplysForm form);

	/**
	 * 修改
	 */
	public Msg update(ExtraworkapplysForm form);
	
	/**
	 * 查询
	 */
	public DataGrid select(ExtraworkapplysForm form);
	
	/**
	 * 加载
	 */
	public ExtraworkapplysForm get(String id);

	/**
	 * 审批通过
	 */
	Msg approvaltrue(String id);

	/**
	 * 审批不通过
	 */
	Msg approvalfalse(String id);

}
