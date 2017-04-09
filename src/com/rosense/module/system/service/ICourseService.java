package com.rosense.module.system.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.AuthForm;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.CourseForm;

public interface ICourseService {

	/**
	 * 添加课程信息
	 */
	public Msg add(CourseForm form);

	/**
	 * 删除课程信息
	 */
	public Msg delete(CourseForm form);

	/**
	 * 修改课程信息
	 */
	public Msg update(CourseForm form);

	/**
	 * 获取一个用户对象
	 */
	public CourseForm get(String id);

	/**
	 * 查询员工
	 */
	public DataGrid datagridCourse(CourseForm form,String selectType,String searchKeyName);
	
	/**
	 * 查询课程集合
	 */
	public List<CourseForm> tree();
	
}
