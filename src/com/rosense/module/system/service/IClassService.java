package com.rosense.module.system.service;

import java.util.List;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.ClassForm;

public interface IClassService {

	/**
	 * 添加组织机构
	 */
	public Msg add(ClassForm form);

	/**
	 * 删除组织机构
	 */
	public Msg delete(ClassForm form);

	/**
	 * 修改组织机构
	 */
	public Msg update(ClassForm form);

	/**
	 * 获取一个组织机构对象
	 */
	public ClassForm get(ClassForm form);

	/**
	 * 根据名称获取班级
	 */
	public ClassForm getByName(String name);

	/**
	 * 生成所有组织机构树
	 */
	public List<ClassForm> tree(String pid);
	
	/**
	 * 根据名字获取班级对象
	 */
	public ClassForm getId(String orgName);
	
	/**
	 * 根据id获取班级及学生
	 */
	public String userAndOrgtree(String pid);
	
	/**
	 * 根据名字获取班级对象
	 */
	public String getName(String id);

}
