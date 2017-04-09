package com.rosense.module.system.service;

import java.util.List;

import com.rosense.basic.model.Msg;
import com.rosense.module.system.entity.ProcedureEntity;
import com.rosense.module.system.web.form.ProcedureForm;

public interface IProcedureService{

	/**
	 * 添加流程菜单
	 * @param form
	 * @return
	 */
	public Msg add(ProcedureForm form);

	/**
	 * 删除流程菜单
	 * @param form
	 * @return
	 */
	public Msg delete(ProcedureForm form);

	public Msg deleteByMenuId(String menuId);
	
	/**
	 * 修改流程菜单
	 * @param form
	 * @return
	 */
	public Msg update(ProcedureForm form);

	/**
	 * 获取一个流程信息
	 * @param form
	 * @return
	 */
	public ProcedureForm get(ProcedureForm form);

	/**
	 * 根据menuId获取对象
	 * @param menuId
	 * @return
	 */
	public ProcedureEntity get(String menuId);

	public ProcedureEntity getById(String id);

	/**
	 * 生成所有菜单树
	 * @param pid 根据父菜单生成菜单树
	 * @return
	 */
	public String treeToJSON(String pid);

	/**
	 * 下拉列表或下拉树
	 * @param form
	 * @return
	 */
	public List<ProcedureForm> combo(ProcedureForm form);

	/**
	 * 生成JSON菜单树
	 * @param form
	 * @param sc
	 * @return
	 */
	public void exportTree();

	/**
	 * 设置菜单是否隐藏
	 * @param form
	 * @return
	 */
	public Msg isShow(ProcedureForm form);

	/**
	 * 更新位置
	 * 如果原位置小于新位置，让所有>原位置，<=新位置的元素全部-1，之后更新对象的位置
	 * 如果原位置大于新位置，让所有<原位置，>=新位置的元素全部加1，之后更新对象位置
	 * @param oldSort 原始的位置
	 * @param newSort 新的位置
	 * @param id 需修改的ID
	 * @param pid 该pid下的sort先加1或减1
	 * @return
	 */
	public void updateResetSort(int oldSort, int newSort, String id, String pid);

	/**
	 * 获取所有菜单数据
	 * @param form
	 * @return
	 */
	public List<ProcedureForm> getAllMenuTree(ProcedureForm form);

	/**
	 * 初始化菜单数据
	 * @param entity
	 */
	public void init(ProcedureEntity entity);
}
