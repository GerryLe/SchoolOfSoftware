package com.rosense.module.system.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.AuthForm;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.UserForm;
import com.rosense.module.system.web.form.AttendanceForm;

public interface IAttendanceService {

	/**
	 * 添加考勤信息
	 */
	public Msg add(List<AttendanceForm> forms);


	/**
	 * 修改考勤信息
	 */
	public Msg update(List<AttendanceForm> forms);

	/**
	 * 获取一个用户对象
	 */
	public AttendanceForm get(String id);

	/**
	 * 查询信息
	 */
	public DataGrid datagrid(AttendanceForm form);
	
	/**
	 * 删除信息
	 */
	public Msg delete(UserForm form);
	
   
}
