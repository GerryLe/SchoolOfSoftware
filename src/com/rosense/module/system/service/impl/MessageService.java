package com.rosense.module.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.MessageEntity;
import com.rosense.module.system.service.IMessageService;
import com.rosense.module.system.web.form.MessageForm;

/**
 * 
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Service("messageService")
@Transactional
public class MessageService implements IMessageService {
	@Inject
	private IBaseDao<MessageEntity> messageDao;

	
	public Msg add(String userId, String content, String type) {
		MessageEntity message = new MessageEntity();
		message.setUserId(userId);
		message.setContent(content);
		message.setType(type);
		this.messageDao.add(message);
		return new Msg(true, "添加成功！");
	}

	
	public Msg add(MessageForm form) {
		MessageEntity entity = new MessageEntity();
		BeanUtils.copyNotNullProperties(form, entity);
		this.messageDao.add(entity);
		return new Msg(true, "添加成功！");
	}

	
	public Msg delete(String ids) {
		try {
			String[] idss = ids.split(",");
			for (String id : idss) {
				this.messageDao.delete(MessageEntity.class, id);
			}
			return new Msg(true, "删除成功！");
		} catch (Exception e) {
			return new Msg(false, "删除失败！");
		}
	}

	
	public Msg update(String id) {
		MessageEntity entity = this.messageDao.load(MessageEntity.class, id);
		if (entity.getId() != null) {
			entity.setReaded(1);
			this.messageDao.update(entity);
		}
		return new Msg(true, "修改成功！");
	}

	
	public Msg updateAll() {
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("userId", WebContextUtil.getCurrentUser().getUser().getUserId());
		this.messageDao.executeSQL("update simple_message t set t.readed=1 where t.userId=:userId", alias);
		return new Msg(true, "修改成功！");
	}

	
	public long count() {
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("userId", WebContextUtil.getUserId());
		return this.messageDao.count("from MessageEntity where readed=0 and userId=:userId", alias, false);
	}

	
	public List<MessageForm> list(MessageForm form) {
		List<MessageForm> list = this.messageDao.listSQL("select * from simple_message t where readed=0 and userId=? order by created desc limit 0,10", new Object[] { WebContextUtil.getUserId() },
				MessageForm.class, false);
		return list;
	}

	
	public DataGrid datagrid(MessageForm form) {
		form.setUserName(WebContextUtil.getCurrentUser().getUser().getUserId());
		if (null == form.getSort()) {
			SystemContext.setSort("t.readed asc,t.created ");
			SystemContext.setOrder("desc");
		} else {
			SystemContext.setSort("t." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		try {
			Pager<MessageForm> pager = this.find(form);
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			throw new ServiceException("加载信息异常", e);
		}
	}

	private Pager<MessageForm> find(MessageForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select * from simple_message t  where userId='"+WebContextUtil.getUserId()+"' ";
		sql = addWhere(sql, form, alias);
		return this.messageDao.findSQL(sql, alias, MessageForm.class, false);
	}

	private String addWhere(String hql, MessageForm form, Map<String, Object> params) {
		if (form.getType() != null && !"".equals(form.getType())) {
			hql += " and t.type like '%" + form.getType() + "%'";
		}
		if (form.getContent() != null && !"".equals(form.getContent())) {
			hql += " and t.content like '%" + form.getContent() + "%'";
		}
		return hql;
	}

}
