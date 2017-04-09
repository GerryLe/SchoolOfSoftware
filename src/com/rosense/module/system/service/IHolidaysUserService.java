package com.rosense.module.system.service;

import java.util.List;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.HolidaysUserForm;

public interface IHolidaysUserService {
	/**
	 * 修改
	 */
	public void update(HolidaysUserForm form);
	/**
	 * 查询
	 */
	public DataGrid get(HolidaysUserForm form);
	
	
	public Msg importFile(List<HolidaysUserForm> importList);
	
	public HolidaysUserForm select(String id);
	
	/**
	 *获取用户角色
	 * @return
	 */
	public int getCurrentUserDefaultRole();
	
	public int equlasValAccount(String account);

}
