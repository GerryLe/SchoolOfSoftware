package com.rosense.module.system.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.IpUtil;
import com.rosense.basic.util.dbutil.IDBUtilsHelper;
import com.rosense.basic.util.springmvc.ContextHolderUtils;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.LogEntity;
import com.rosense.module.system.entity.LoginLogEntity;
import com.rosense.module.system.service.ILogService;
import com.rosense.module.system.web.form.LogForm;
import com.rosense.module.system.web.form.LoginLogForm;

/**
 * 包含了两张表的操作：日志管理表，用户登录日志管理表
 */
@Service
@Transactional
public class LogServiceImpl implements ILogService {
	@Autowired
	private IBaseDao<LogEntity> logDao;
	@Autowired
	private IBaseDao<LoginLogEntity> loginLogDao;
	@Autowired
	private IDBUtilsHelper dbUtil;

	
	public void add(String title, String detail) {
		final LogEntity log = new LogEntity();
		log.setUserId(WebContextUtil.getUserId());
		log.setCreated(new Date());
		log.setTitle(title);
		log.setDetail(detail);
		log.setIp(IpUtil.getIpAddr(ContextHolderUtils.getRequest()));
		this.logDao.add(log);
	}

	
	public Msg delete(LogForm form) {
		try {
			if (null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",");
				for (String id : ids) {
					this.logDao.delete(LogEntity.class, id);
				}
				return new Msg(true, "删除成功！");
			} else {
				return new Msg(false, "删除失败！");
			}
		} catch (BeansException e) {
			return new Msg(false, "删除失败！");
		}
	}

	
	public DataGrid datagrid(LogForm form) {
		if (null == form.getSort()) {
			SystemContext.setSort("t.created");
			SystemContext.setOrder("desc");
		} else {
			SystemContext.setSort("t." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		try {
			Pager<LogForm> pager = this.find(form);
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("加载列表信息异常" + e);
		}
	}

	private Pager<LogForm> find(LogForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select t.*,u.name userName from simple_log t left join simple_user u on t.userId=u.id where 1=1 ";
		return this.logDao.findSQL(sql, alias, LogForm.class, false);
	}

	/*********************************************************************************/

	
	public Msg addLL(LoginLogForm form) {
		try {
			LoginLogEntity entity = new LoginLogEntity();
			BeanUtils.copyNotNullProperties(form, entity);
			entity.setIp(IpUtil.getIpAddr(ContextHolderUtils.getRequest()));
			this.loginLogDao.add(entity);
		} catch (BeansException e) {
			return new Msg(false, "发生错误！");
		}
		return new Msg(false, "编辑失败！");
	}

	
	public Msg deleteLL(LoginLogForm form) {
		try {
			if (null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",");
				for (String id : ids) {
					this.loginLogDao.delete(LoginLogEntity.class, id);
				}
				return new Msg(true, "删除成功！");
			} else {
				return new Msg(false, "删除失败！");
			}
		} catch (BeansException e) {
			return new Msg(false, "删除失败！");
		}
	}

	
	public LoginLogForm getLL(LoginLogForm form) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select t from LoginLogEntity t where 1=1";

		LoginLogEntity entity = (LoginLogEntity) this.loginLogDao.queryObject(hql, params);
		if (null != entity) {
			LoginLogForm pform = new LoginLogForm();
			BeanUtils.copyProperties(entity, pform);

			return pform;
		} else {
			return null;
		}
	}

	
	public DataGrid datagridLL(LoginLogForm form) {
		if (null == form.getSort()) {
			SystemContext.setSort("t.created");
			SystemContext.setOrder("desc");
		} else {
			SystemContext.setSort("t." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		try {
			Pager<LoginLogForm> pager = this.find(form);
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("加载人员列表信息异常：" + e.getMessage());
		}
	}

	private Pager<LoginLogForm> find(LoginLogForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select t.*,u.name userName from simple_log_login t left join simple_user u on t.userId=u.id where 1=1 ";
		return this.loginLogDao.findSQL(sql, alias, LoginLogForm.class, false);
	}

}
