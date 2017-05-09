package com.rosense.module.system.service;

import java.util.List;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.MessageForm;

/**
 * 
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public interface IMessageService {

	public Msg add(MessageForm form);

	/**
	 * 保存消息
	 */
	public Msg add(String userId, String type, String content);

	public Msg delete(String ids);

	public Msg update(String id);

	public Msg updateAll();

	public long count();

	public DataGrid datagrid(MessageForm form);

	public List<MessageForm> list(MessageForm form);


}
