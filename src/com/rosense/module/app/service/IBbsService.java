package com.rosense.module.app.service;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.app.web.form.BbsForm;
import com.rosense.module.app.web.form.BbsItemForm;

/**
 *用户互动
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public interface IBbsService {
	/**
	 * 获取帖子
	 */
	BbsForm get(String id);

	/**
	 * 查询增加查看次数
	 */
	BbsForm getView(String id);

	/**
	 * 发布帖子
	 */
	Msg add(BbsForm form);

	/**
	 * 删除帖子
	 */
	Msg delete(String id);

	/**
	 * 推荐
	 */
	Msg recommend(String id, boolean recommend);

	/**
	 * 回复帖子
	 */
	Msg addItem(BbsItemForm form);

	/**
	 * 删除帖子回复
	 */
	Msg deleteItem(String id);

	/**
	 * 点赞帖子条目
	 */
	Msg zanItem(String id, String sessionId);

	/**
	 * 点赞帖子
	 */
	Msg zan(String id, String sessionId);

	/**
	 * 获取帖子列表
	 */
	DataGrid datagrid(BbsForm form);

	/**
	 * 查看我的帖子
	 */
	DataGrid datagridmy(BbsForm form);

	/**
	 * 管理员查看帖子
	 */
	DataGrid datagridmanager(BbsForm form);

	/**
	 * 获取帖子回复列表
	 */
	DataGrid datagrid(BbsItemForm form);

}
