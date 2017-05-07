package com.rosense.module.system.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.AttendanceForm;
import com.rosense.module.system.web.form.UserForm;

public interface IAttendanceService {

	/**
	 * 添加考勤信息
	 */
	public Msg add(AttendanceForm form);


	/**
	 * 修改考勤信息
	 */
	public Msg update(AttendanceForm form);

	/**
	 * 获取一个用户对象
	 */
	public AttendanceForm get(String id);

	/**
	 * 查询信息
	 */
	public DataGrid datagrid(AttendanceForm form);
	
	/**
	 * 查询个人信息
	 */
	public DataGrid datagridPersonal(AttendanceForm form);
	
	/**
	 * 删除信息
	 */
	public Msg delete(UserForm form);
}
	
   
