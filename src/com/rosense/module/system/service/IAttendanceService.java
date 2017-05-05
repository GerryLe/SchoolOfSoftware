package com.rosense.module.system.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.AuthForm;
import com.rosense.module.system.web.form.ClassForm;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.AttendanceForm;

public interface IAttendanceService {

	/**
	 * 添加考勤信息
	 */
	public Msg add(List<AttendanceForm> listForm);

	/**
	 * 删除考勤信息
	 */
	public Msg delete(AttendanceForm form);

	/**
	 * 修改考勤信息
	 */
	public Msg update(List<AttendanceForm> listForm);

	/**
	 * 获取一个用户对象
	 */
	public AttendanceForm get(String id);

	/**
	 * 查询考勤
	 */
	public DataGrid datagridAttendance(AttendanceForm form,String selectType,String searchKeyName);
	
	

}
