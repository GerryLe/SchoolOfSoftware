package com.rosense.module.system.service;

import java.util.List;

import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.PositionForm;

public interface IPositionService {
	
	/**
	 * 添加岗位
	 */
	public Msg add(PositionForm form) ;
	
	/**
	 * 删除岗位
	 */
	public Msg delete(PositionForm form) ;
	
	/**
	 * 修改岗位
	 */
	public Msg update(PositionForm form) ;
	
	/**
	 * 获取一个岗位对象
	 */
	public PositionForm get(PositionForm form) ;
	
	/**
	 * 生成所有岗位树
	 */
	public List<PositionForm> tree(String pid) ;
	
	/**
	 * 根据名称获取职位对象
	 */
	public PositionForm getId(String positionName);
	
}
