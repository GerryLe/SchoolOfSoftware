package com.rosense.module.app.service.impl;

import java.net.URLDecoder;
import java.util.HashMap;
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
import com.rosense.basic.util.ClobUtil;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.app.entity.BbsEntity;
import com.rosense.module.app.entity.BbsItemEntity;
import com.rosense.module.app.service.IBbsService;
import com.rosense.module.app.web.form.BbsForm;
import com.rosense.module.app.web.form.BbsItemForm;
import com.rosense.module.common.web.servlet.WebContextUtil;

@Service
@Transactional
public class BbsService implements IBbsService {
	@Inject
	private IBaseDao<BbsEntity> bbsDao;
	@Inject
	private IBaseDao<BbsItemEntity> bbsItemDao;

	
	public BbsForm get(String id) {
		BbsEntity entity = this.bbsDao.load(BbsEntity.class, id);
		BbsForm form = new BbsForm();
		BeanUtils.copyNotNullProperties(entity, form, new String[] { "content" });
		form.setContent(ClobUtil.getString(entity.getContent()));
		return form;
	}

	
	public BbsForm getView(String id) {
		BbsEntity entity = this.bbsDao.load(BbsEntity.class, id);
		BbsForm form = new BbsForm();
		entity.setViews(entity.getViews() + 1);
		this.bbsDao.update(entity);
		BeanUtils.copyNotNullProperties(entity, form, new String[] { "content" });
		form.setContent(ClobUtil.getString(entity.getContent()));
		return form;
	}

	
	public Msg add(BbsForm form) {
		if (WebContextUtil.addcaches.containsKey(form.getSessionId() + "1")) {
			return new Msg(false, "操作太频繁，休息一分钟");
		}
		WebContextUtil.addcaches.put(form.getSessionId() + "1", System.currentTimeMillis());
		BbsEntity bbsEntity = new BbsEntity();
		BeanUtils.copyNotNullProperties(form, bbsEntity, new String[] { "content" });
		bbsEntity.setFloor(1);
		bbsEntity.setContent(ClobUtil.getClob(form.getContent()));
//		bbsEntity.setUserId(WebContextUtil.getUserId());
		bbsEntity.setId(String.valueOf(WebContextUtil.getNextId("bbs")));
		this.bbsDao.add(bbsEntity);
		Msg msg = new Msg(true, "添加成功");
		msg.setObj(bbsEntity.getId());
		return msg;
	}

	
	public Msg recommend(String id, boolean recommend) {
		BbsEntity entity = this.bbsDao.load(BbsEntity.class, id);
		entity.setRecommend(recommend);
		this.bbsDao.update(entity);
		return new Msg(true, "推荐成功");
	}

	
	public Msg delete(String id) {
		BbsEntity entity = this.bbsDao.load(BbsEntity.class, id);
		if (WebContextUtil.getUserId().equals(entity.getUserId())) {
			this.bbsDao.delete(entity);
			this.bbsDao.executeSQL("delete from simple_bbs_item where bbsId=?", new Object[] { id });
			return new Msg(true, "没有权限");
		}
		return new Msg(false, "没有权限");
	}

	
	public Msg zan(String id, String sessionId) {
		String t = id + sessionId;
		if (WebContextUtil.caches.contains(t)) {
			return new Msg(false);
		}
		WebContextUtil.caches.add(t);
		final BbsEntity bbs = this.bbsDao.load(BbsEntity.class, id);
		bbs.setZans(bbs.getZans() + 1);
		this.bbsDao.update(bbs);
		return new Msg(true);
	}

	
	public Msg zanItem(String id, String sessionId) {
		String t = id + sessionId;
		if (WebContextUtil.caches.contains(t)) {
			return new Msg(false);
		}
		WebContextUtil.caches.add(t);
		final BbsItemEntity itemEntity = this.bbsItemDao.load(BbsItemEntity.class, id);
		itemEntity.setZans(itemEntity.getZans() + 1);
		this.bbsItemDao.update(itemEntity);
		return new Msg(true);
	}

	
	public Msg addItem(BbsItemForm form) {
		if (WebContextUtil.addcaches.containsKey(form.getSessionId() + "2")) {
			return new Msg(false, "操作太频繁，休息一分钟");
		}
		WebContextUtil.addcaches.put(form.getSessionId() + "2", System.currentTimeMillis());
		final BbsItemEntity entity = new BbsItemEntity();
		final BbsEntity bbs = this.bbsDao.load(BbsEntity.class, form.getBbsId());
		BeanUtils.copyNotNullProperties(form, entity, new String[] { "content" });
		/*entity.setUserId(WebContextUtil.getUserId());*/
		entity.setContent(ClobUtil.getClob(form.getContent()));
		entity.setId(String.valueOf(WebContextUtil.getNextId("bbs")));
		entity.setFloor(bbs.getFloor());
		this.bbsItemDao.add(entity);

		bbs.setRemarks(bbs.getRemarks() + 1);
		bbs.setFloor(bbs.getFloor() + 1);
		this.bbsDao.update(bbs);

		return new Msg(true, "添加成功");
	}

	
	public Msg deleteItem(String id) {
		final BbsItemEntity itemEntity = this.bbsItemDao.load(BbsItemEntity.class, id);
		final BbsEntity bbs = this.bbsDao.load(BbsEntity.class, itemEntity.getBbsId());
		bbs.setRemarks(bbs.getRemarks() - 1);
		this.bbsDao.update(bbs);
		itemEntity.setDeleted(true);
		this.bbsItemDao.update(itemEntity);
		return new Msg(true, "删除成功");
	}

	
	public DataGrid datagridmy(BbsForm form) {
		if (null == form.getSort()) {
			SystemContext.setSort("t.createDate ");
			SystemContext.setOrder("desc");
		}
		try {
			String sql = "select * from simple_bbs t  where userId=?";
			Pager<BbsForm> pager = this.bbsDao.findSQL(sql, new Object[] { WebContextUtil.getUserId() }, BbsForm.class, false);
			for (BbsForm bbs : pager.getDataRows()) {
				bbs.setFormatDate(DateUtils.getRelativeDate(bbs.getUpdateDate()));
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			throw new ServiceException("加载信息异常", e);
		}
	}

	
	public DataGrid datagridmanager(BbsForm form) {
		if (null == form.getSort()) {
			SystemContext.setSort("t.createDate ");
			SystemContext.setOrder("desc");
		}
		try {
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select * from simple_bbs t  where 1=1 ";
			if (StringUtil.isNotEmpty(form.getTitle())) {
				try {
					alias.put("title", "%%" + URLDecoder.decode(form.getTitle(), "UTF-8") + "%%");
					sql += " and t.title like :title ";
				} catch (Exception e) {
				}
			}
			Pager<BbsForm> pager = this.bbsDao.findSQL(sql, alias, BbsForm.class, false);
			for (BbsForm bbs : pager.getDataRows()) {
				bbs.setFormatDate(DateUtils.getRelativeDate(bbs.getUpdateDate()));
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			throw new ServiceException("加载信息异常", e);
		}
	}

	
	public DataGrid datagrid(BbsForm form) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			if (!"recommend".equals(form.getFilter())) {
				form.setSort(form.getFilter());
			}
		}
		if (null == form.getSort()) {
			SystemContext.setSort("t.createDate ");
			SystemContext.setOrder("desc");
		} else {
			SystemContext.setSort("t." + form.getSort());
			SystemContext.setOrder("desc");
		}
		try {
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select * from simple_bbs t  where 1=1 ";
			if ("recommend".equals(form.getFilter())) {
				sql += "  and recommend=1";
			}
			if (StringUtil.isNotEmpty(form.getTitle())) {
				alias.put("title", "%%" + form.getTitle() + "%%");
				sql += " and t.title like :title ";
			}
			Pager<BbsForm> pager = this.bbsDao.findSQL(sql, alias, BbsForm.class, false);
			for (BbsForm bbs : pager.getDataRows()) {
				bbs.setFormatDate(DateUtils.getRelativeDate(bbs.getUpdateDate()));
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			throw new ServiceException("加载信息异常", e);
		}
	}

	
	public DataGrid datagrid(BbsItemForm form) {
		if (null == form.getSort()) {
			SystemContext.setSort("t.createDate ");
			SystemContext.setOrder("asc");
		} else {
			SystemContext.setSort("t." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		try {
			String sql = "select * from simple_bbsitem t where bbsId=? and deleted=0";
			Pager<BbsItemForm> pager = this.bbsDao.findSQL(sql, new Object[] { form.getBbsId() }, BbsItemForm.class, false);
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			throw new ServiceException("加载信息异常", e);
		}
	}

}
