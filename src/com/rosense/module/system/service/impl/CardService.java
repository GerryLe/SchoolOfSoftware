package com.rosense.module.system.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.CardEntity;
import com.rosense.module.system.entity.OrgEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.ICardService;
import com.rosense.module.system.web.form.CardForm;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

import freemarker.template.utility.DateUtil;
import net.sf.json.JSONObject;
/**
 * 删除用户操作，只删除用户账号的数据，用户对于的员工信息见不会删除，
 * 员工删除操作需员工管理页面下进行操作
 *
 */
@Service("cardService")
@Transactional
public class CardService extends BaseService implements ICardService {
	private Logger logger = Logger.getLogger(CardService.class);
	@Inject
	private IBaseDao<CardEntity> cardDao;
	@Inject
	private IBaseDao<OrgEntity> orgDao;
	@Inject
	private IBaseDao<RoleEntity> roleDao;
	@Inject
	private IBaseDao<UserEntity> userDao;
	
	public DataGrid datagridcard(CardForm form){
		try {
			
			List<Integer> roleId=new ArrayList<Integer>();
			List<String> roleName=new ArrayList<String>();
			//判断当前用户是否具有多个角色
			if(WebContextUtil.getCurrentUser().getUser().getRole_ids().contains(",")){
				String[] roleIds=WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
				for(String id:roleIds){
					RoleForm role=(RoleForm) this.roleDao.queryObjectSQL("select * from simple_role where id=?",new Object[]{id},RoleForm.class,false);
					if(role!=null){
						roleId.add(role.getDefaultRole());
						roleName.add(role.getSn());
					}
					
				}
				
			}else{
				RoleEntity role=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
				roleId.add(role.getDefaultRole());
				roleName.add(role.getSn());
			}
			
		   //RoleEntity role=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
		   UserForm curform=(UserForm) this.userDao.queryObjectSQL("select e.* from simple_user u left join simple_person e on(u.personId=e.id) where u.id='"+WebContextUtil.getCurrentUser().getUser().getId()+"'",UserForm.class,false);
			List<CardForm> forms = new ArrayList<CardForm>();
		  	Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select c.*, p.workType workType from simple_org o right join simple_card c on(o.name=c.orgName) left join simple_position p ON(c.positionName=p.name) where c.status=0 ";
			if(roleId.contains(3)){
				sql+="";
			}else if(!StringUtil.isEmpty(curform.getOrgChildId())){
				if(roleId.contains(2)){
					sql+="  and c.orgName in('" + this.getOrgName(curform.getOrgChildId()) + "'";
					sql=getOrgNameByPid(sql,curform.getOrgChildId());
					sql+=")";
				   }
				if(roleId.contains(4)){
					sql+="  and c.orgName in('" + this.getOrgHeadName(curform.getOrgChildId()) + "'";
					sql=getOrgNameByPid(sql,curform.getOrgChildId());
					sql+=") ";
				   }
			}
			sql = addWhere(sql, form, alias);
			Pager<CardForm> pager = this.cardDao.findSQL(sql, alias, CardForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (CardForm pf : pager.getDataRows()) {
					if(StringUtils.isNotEmpty(pf.getStartTime())&&StringUtils.isNotEmpty(pf.getEndTime())&&StringUtils.isNotEmpty(pf.getRecordDate())){
						String dateString=pf.getRecordDate().replace("/", "-");
						String d1= dateString+" "+pf.getStartTime();
						String d2=dateString+" "+pf.getEndTime();
						pf.setWorkTime(DateUtils.dateDiffStr(d1,d2, DateUtils.DEFAULT_DATE_HH_MM_PATTERN));
					}
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载考勤信息失败===>异常信息：", e);
			throw new ServiceException("加载考勤信息异常：", e);
		}
	}

	
	private String addWhere(String sql, CardForm form, Map<String, Object> params) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if(key.equals("name")){
					form.setName(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if(key.equals("account")){
					form.setAccount(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if(key.equals("recordDate")){
					form.setRecordDate(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if(key.equals("orgName")){
					form.setOrgName(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
			}
		}
		if (StringUtil.isNotEmpty(form.getName())) {
			try {
				params.put("name", "%%" + URLDecoder.decode(form.getName(), "UTF-8") + "%%");
				sql += "and c.name like :name";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (StringUtil.isNotEmpty(form.getAccount())) {
			try {
				params.put("account", "%%" + URLDecoder.decode(form.getAccount(), "UTF-8") + "%%");
				sql += " and c.account like :account ";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (StringUtil.isNotEmpty(form.getRecordDate())) {
			try {
				params.put("recordDate", "%%" + URLDecoder.decode(form.getRecordDate(), "UTF-8") + "%%");
				sql += "and c.recordDate like :recordDate";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (StringUtil.isNotEmpty(form.getOrgName())) {
			try {
				params.put("orgName", "%%" + URLDecoder.decode(form.getOrgName(), "UTF-8") + "%%");
				sql += " and c.orgName like :orgName ";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sql;
	}
	
	@Override
	public Msg importFile(List<CardForm> importUserList) {
		// TODO Auto-generated method stub
		try {
            for (int i = 0; i < importUserList.size(); i++) {
            	CardForm cardForm = importUserList.get(i);
            	int equalsAccount = this.equlasVal(cardForm.getAccount(),cardForm.getRecordDate());
    			if (equalsAccount == 1){
    					cardForm.setId(this.getUserId(cardForm.getAccount(),cardForm.getRecordDate()));
    					this.update(cardForm);
    			}
    			else{
    				this.addUserCard(cardForm);
    				}
            }
            return new Msg(true, "导入打卡信息成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入打卡信息失败===>异常信息：", e);
			return new Msg(false, "导入打卡信息失败！");
		}
	}
	
	private int equlasVal(String param1,String param2) {
		String sql = "select c.* from simple_card c where c.account='" + param1 + "' and c.recordDate='"+param2+"'";
		return this.cardDao.countSQL(sql, false).intValue();
	}

	private String getUserId(String param1,String param2) {
		String idString = null;
		String sql = "select c.* from simple_card c where c.account='" + param1 + "' and c.recordDate='"+param2+"'";
		List<CardForm> list = this.cardDao.listSQL(sql, CardForm.class, false);
		if (null != list && list.size() > 0) {
			for (CardForm e : list) {
				 idString=e.getId();
			}
		}
		return idString;
	}
	
	public Msg addUserCard(CardForm form) {
		try {
			final CardEntity c = new CardEntity();
			BeanUtils.copyNotNullProperties(form, c);
			c.setStatus(0);
			this.cardDao.add(c);
			return new Msg(true, "添加成功！");
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("添加用户打卡信息失败===>异常信息：", e);
			return new Msg(false, "添加失败！");
		}
	}

	
	
	//更新信息
	public Msg update(CardForm form) {
		try { 
			final CardEntity c = new CardEntity();
			c.setStatus(0);
			BeanUtils.copyNotNullProperties(form, c);
			this.cardDao.update(c);
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改打卡信息失败===>异常信息：", e);
			return new Msg(false, "修改打卡信息失败！");
		}
	}

	
	@Override
	public DataGrid orgdatagridcard(HttpSession session,CardForm form){
		try {
			LoginSession user = (LoginSession) session.getAttribute(Const.USER_SESSION);
			String orgId=user.getUser().getOrgId();
			List<CardForm> forms = new ArrayList<CardForm>();
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select c.*, p.workType workType from simple_card c left join simple_position p ON(c.positionName=p.name) where c.status=0 ";
			sql = addWhere(sql, form, alias);
			sql+="  and c.orgName in('" + this.getOrgName(orgId) + "'";
			sql=getOrgNameByPid(sql,orgId);
			sql+=")";
			Pager<CardForm> pager = this.cardDao.findSQL(sql, alias, CardForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (CardForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载考勤信息失败===>异常信息：", e);
			throw new ServiceException("加载考勤信息异常：", e);
		}
	
	}
	
	private String getOrgNameByPid(String sql,String pid){
		 String sqlorg = "select t.* from simple_org t where t.pid='" + pid + "'";
		 List<OrgForm> orgs = this.orgDao.listSQL(sqlorg, OrgForm.class, false);
			if (null != orgs && orgs.size() > 0) {
				for (OrgForm e : orgs) {
					sql+=",'"+e.getName()+"'";
					sql=getOrgNameByPid(sql,e.getId());
					}
			}else{
				
			}
		return sql;
	}
	
	private String getOrgName(String pid){
		String name=null;
		String sqlorg = "select t.* from simple_org t where t.id='" + pid + "'";
		 List<OrgForm> orgs = this.orgDao.listSQL(sqlorg, OrgForm.class, false);
			if (null != orgs && orgs.size() > 0) {
				for (OrgForm e : orgs) {
					 name=e.getName();
					}
			}
			return name;
	}
	
	private String getOrgHeadName(String pid){
		String name=null;
		String sqlorg = "select t.* from simple_org t where t.id='" + pid + "'";
		 List<OrgForm> orgs = this.orgDao.listSQL(sqlorg, OrgForm.class, false);
			if (null != orgs && orgs.size() > 0) {
				for (OrgForm e : orgs) {
					 name=e.getName();
					}
			}
			return name;
	}
}
