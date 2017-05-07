package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.ScoreForm;
import com.rosense.module.system.web.form.UserForm;

public interface IScoreService {

	/**
	 * 添加信息
	 */
	public Msg add(ScoreForm form);


	/**
	 * 修改信息
	 */
	public Msg update(ScoreForm form);

	/**
	 * 获取一个用户对象
	 */
	public ScoreForm get(String id);

	/**
	 * 查询信息
	 */
	public DataGrid datagrid(ScoreForm form);
	
	/**
	 * 删除信息
	 */
	public Msg delete(UserForm form);
}
	
   
