package com.rosense.module.system.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.BaseDao;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.SendEmailUtil;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.ResignationEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.service.IResignationService;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.ResignationForm;

import net.sf.json.JSONObject;

@Transactional
@Service
public class ResignationService implements IResignationService {
	@Inject
	private BaseDao<ResignationEntity> resignationDao ;
	@Inject
	private BaseDao<RoleEntity> roleDao;
	
	private String fistAudit = "shadow han";
	private String secondAudit = "may luo";
	private String thirdAudit = "ravic li";

	@Override
	public DataGrid datagridresignation(ResignationForm form) {
		try {
			String userName = WebContextUtil.getUserName();
			ResignationForm mine = getAuditById("");
			String extre = null;//根据状态state以及是否为主管进行划分
			if(fistAudit.equals(userName.toLowerCase())){
				extre = "(state='1' and valid=true) or (state='0' and immediateBoss='"+userName+"') and valid=true ";
			}else if(secondAudit.equals(userName.toLowerCase())){
				extre = "(state='2' and flag=true) or (state='0' and immediateBoss='"+userName+"') and valid=true ";
			}else if(thirdAudit.equals(userName.toLowerCase())){
				extre = "(state='3' and flag=true) or (state='0' and immediateBoss='"+userName+"') and valid=true ";
			}else{
				extre = "state='0' and immediateBoss='"+userName+"' and valid=true ";
			}
			SystemContext.setSort("chinaname");
			SystemContext.setOrder("desc");
			Map<String, Object> alias = new HashMap<String, Object>();
			List<ResignationForm> forms = new ArrayList<ResignationForm>();
			String sql = "select * from resignation where "+ extre;
			if(null!=form.getSearch()){
				String search = new String(form.getSearch().getBytes("iso8859-1"),"utf-8");
				sql += "and "+form.getSelect()+" like '%%"+search+"%%' ";
				
			}
			Pager<ResignationForm> pager = resignationDao.findSQL(sql, alias, ResignationForm.class, false);
			if(null != mine){
				pager.getDataRows().add(mine);
				pager.setTotal(pager.getTotal()+1);
			}
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (ResignationForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			return null;
		}
	}

	private String addWhere(String sql, ResignationForm form, Map<String, Object> params) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if(key.equals("chinaname")){
					form.setChinaname(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if(key.equals("name")){
					form.setName(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
			}
		}
		if (StringUtil.isNotEmpty(form.getChinaname())) {
			try {
				params.put("chinaname", "%%" + URLDecoder.decode(form.getChinaname(), "UTF-8") + "%%");
				sql += " and r.chinaname like :chinaname";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (StringUtil.isNotEmpty(form.getName())) {
			try {
				params.put("name", "%%" + URLDecoder.decode(form.getName(), "UTF-8") + "%%");
				sql += " and r.name like :name ";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sql;
	}

	@Override
	public Msg add(ResignationForm form) {
		try {
			if(null==form.getWorkingAge()||form.getWorkingAge()<=0)//工龄可能需要限制，（离职日期-入职日期）
				return new Msg(false,"工龄只能为正数");
			if(null==form.getImmediateBoss()||"".equals(form.getImmediateBoss().trim())){
				return new Msg(false,"直属上级不能为空");
			}else{
				if(form.getImmediateBoss().trim().equals(form.getName()))
					return new Msg(false,"直属上级有误");
				String sql = "select r.defaultRole as 'defaultRole' from simple_role r right join simple_user_roles ur on(r.id=ur.roleId) right join simple_user u on(u.id=ur.userId) where u.name='"+form.getImmediateBoss()+"'";//判断上级是否主管以上级别
				Session session = resignationDao.getCurrentSession();
				List<Object> ls = session.createSQLQuery(sql).list();
				Iterator iterator = ls.iterator();
				if(!iterator.hasNext())
					return new Msg(false,"不存在该员工");
				while(iterator.hasNext()){
					int defaultRole =  (int) iterator.next();
					if(defaultRole==1)//普通员工角色号为1，可能需添加排除其他角色
						return new Msg(false,"直属上级应为主管以上级别");
				}
			}
			if(null==form.getResignationReason()||"".equals(form.getResignationReason().trim()))
				return new Msg(false,"离职原因不能为空");
			if(null==form.getPredictResignationDate()||"".equals(form.getPredictResignationDate()))
				return new Msg(false,"预计离职日期不能为空");
			if(null!=loadByPersonIdandState(form.getPersonId(),true)){//申请存在时，更新
				ResignationEntity entity = loadByPersonIdandState(form.getPersonId(),true);
				if(entity.getState()>=1)
					return new Msg(false,"正在审批，请等待审核结果");
				else if(entity.getState()==-1){
					entity.setValid(false);
					resignationDao.update(entity);
					ResignationEntity newEntity = new ResignationEntity();
					BeanUtils.copyNotNullProperties(entity, newEntity);
					newEntity.setId(null);
					newEntity.setArea(form.getArea());
					newEntity.setWorkingAge(form.getWorkingAge());
					newEntity.setPredictResignationDate(form.getPredictResignationDate());
					newEntity.setResignationReason(form.getResignationReason());
					newEntity.setImmediateBoss(form.getImmediateBoss());
					newEntity.setState(0);
					newEntity.setValid(true);
					newEntity.setSta(0);
					newEntity.setSta1(0);
					newEntity.setSta2(0);
					newEntity.setSta3(0);
					newEntity.setDirectAuditOpinion(null);
					newEntity.setDirectRemark(null);
					newEntity.setEmailDeal(null);
					newEntity.setAuditOpinion1(null);
					newEntity.setRemark1(null);
					newEntity.setAuditOpinion2(null);
					newEntity.setRemark2(null);
					newEntity.setAuditOpinion3(null);
					newEntity.setRemark3(null);
					resignationDao.add(newEntity);
					return new Msg(true,"修改成功，请等待审核结果");
				}
				entity.setArea(form.getArea());
				entity.setWorkingAge(form.getWorkingAge());
				entity.setPredictResignationDate(form.getPredictResignationDate());
				entity.setResignationReason(form.getResignationReason());
				entity.setImmediateBoss(form.getImmediateBoss());
				resignationDao.update(entity);
				return new Msg(true,"修改成功，请等待审核结果");
			}else{
				final ResignationEntity entity = new ResignationEntity();
				BeanUtils.copyNotNullProperties(form, entity);
				resignationDao.add(entity);
				return new Msg(true,"申请成功，请等待审核结果");
			}
		} catch (Exception e) {
			return new Msg(false,"申请失败");
		}
	}

	private ResignationEntity loadByPersonIdandState(String personId,boolean valid){
		String sql = "select * from resignation where personId='"+personId+"' and valid="+valid+"";
		return (ResignationEntity) resignationDao.queryObjectSQL(sql, ResignationEntity.class, true);
	}
	//地区未划分
	@Override
	public ResignationForm getresignation() {
		try {
			String personId = WebContextUtil.getCurrentUser().getUser().getPersonId();
			ResignationEntity entity = loadByPersonIdandState(personId,true);
			ResignationForm form = new ResignationForm();
			if(null!=entity){
				BeanUtils.copyNotNullProperties(entity, form);
				form.setApplyDate(new Date());
				return form;
			}
			String[] ids = WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
			RoleEntity role = null;
			//获取基本角色{总经理，经理，部门总监，部门主管，普通员工}
			for (int i = 0; i < ids.length; i++) {
				role = this.roleDao.load(RoleEntity.class, ids[i]);
				if("56421".indexOf(role.getDefaultRole()+"")>-1)
					break;
			}
			String sql = "select per.chinaname,per.employmentDate,cor.name as 'childOrgName',por.name as 'parentOrgName',pos.name as 'positionName',ur.roleId as 'roleId',ro.name as 'roleName',us.account,ro.defaultRole,per.name,per.area ";
			sql	+= "from simple_person per left join simple_org cor on(per.orgId=cor.id) left join simple_org por on(cor.pid=por.id) left join simple_position pos on(pos.id=per.positionId) left join simple_user us on(us.personId=per.id) left join simple_user_roles ur on(ur.userId=us.id) left join simple_role ro on(ro.id=ur.roleId)";
			sql	+= "where per.id='"+personId+"' and ro.id='"+role.getId()+"'";
			ResignationForm baseForm = (ResignationForm) this.resignationDao.queryObjectSQL(sql, ResignationForm.class, false);
				form.setAccount(baseForm.getAccount());
				form.setChinaname(baseForm.getChinaname());
				form.setName(baseForm.getName());
				form.setPositionName(baseForm.getPositionName());
				form.setChildOrgName(baseForm.getChildOrgName());
				form.setParentOrgName(baseForm.getParentOrgName());
				form.setEmploymentDate(baseForm.getEmploymentDate());
				form.setApplyDate(new Date());
				form.setArea(baseForm.getArea());
				if(baseForm.getDefaultRole()!=1)//非普通员工时，显示may,ravic审核表单
					form.setFlag(true);
			return form;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Msg addAudit(ResignationForm form) {
		try {
			String userName = WebContextUtil.getUserName();
			ResignationEntity entity = resignationDao.load(ResignationEntity.class, form.getId());

			if(fistAudit.equals(userName.toLowerCase())&&entity.getState()==1){
				entity.setRemark1(form.getDirectRemark());
				entity.setAuditOpinion1(form.getDirectAuditOpinion());
				entity.setSta1(form.getSta());
				if(entity.getSta1()==2)
					entity.setState(-1);
				else
					entity.setState(entity.getState()+1);
//				if(!entity.isFlag())
//					this.sendEmail(title, content, str);
			}else if(secondAudit.equals(userName.toLowerCase())&&entity.getState()==2){
				entity.setRemark2(form.getDirectRemark());
				entity.setAuditOpinion2(form.getDirectAuditOpinion());
				entity.setSta2(form.getSta());
				if(entity.getSta2()==2)
					entity.setState(-1);
				else
					entity.setState(entity.getState()+1);
			}else if(thirdAudit.equals(userName.toLowerCase())&&entity.getState()==3){
				entity.setRemark3(form.getDirectRemark());
				entity.setAuditOpinion3(form.getDirectAuditOpinion());
				entity.setSta3(form.getSta());
				if(entity.getSta3()==2)
					entity.setState(-1);
				else
					entity.setState(entity.getState()+1);
			}else{//直属上级
				entity.setSta(form.getSta());
				entity.setEmailDeal(form.getEmailDeal());
				entity.setDirectRemark(form.getDirectRemark());
				entity.setDirectAuditOpinion(form.getDirectAuditOpinion());
				if(entity.getSta()==2)
					entity.setState(-1);
				else
					entity.setState(entity.getState()+1);
			}

			resignationDao.update(entity);
			return new Msg(true,"编辑成功");
		} catch (Exception e) {
			return new Msg(false,"编辑失败");
		}
	}

	public ResignationForm getAudit(ResignationForm form){
		try {
			String userName = WebContextUtil.getUserName();
			ResignationEntity entity = resignationDao.load(ResignationEntity.class, form.getId());
			if(fistAudit.equals(userName.toLowerCase())&&entity.getState()==1){
				form.setDirectRemark(entity.getRemark1());
				form.setDirectAuditOpinion(entity.getAuditOpinion1());
				form.setSta(entity.getSta1());
			}else if(secondAudit.equals(userName.toLowerCase())&&entity.getState()==2){
				form.setDirectRemark(entity.getRemark2());
				form.setDirectAuditOpinion(entity.getAuditOpinion2());
				form.setSta(entity.getSta2());
			}else if(thirdAudit.equals(userName.toLowerCase())&&entity.getState()==3){
				form.setDirectRemark(entity.getRemark3());
				form.setDirectAuditOpinion(entity.getAuditOpinion3());
				form.setSta(entity.getSta3());
			}else{//直属上级
				form.setDirectRemark(entity.getDirectRemark());
				form.setDirectAuditOpinion(entity.getDirectAuditOpinion());
				form.setSta(entity.getSta());
				form.setEmailDeal(entity.getEmailDeal());
				form.setShowExtre(true);
			}
			return form;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<ResignationForm> getImmediateBoss(String name) {
		try {
			String[] ids = WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
			RoleEntity role = null;
			Integer directRole = null;
			for (int i = 0; i < ids.length; i++) {
				role = this.roleDao.load(RoleEntity.class, ids[i]);
				int index = "56421".indexOf(role.getDefaultRole()+"");
				if(index>-1){
					if(role.getDefaultRole()==5)
						directRole = 5;
					else
						directRole = Integer.parseInt("56421".toCharArray()[index-1]+"");
					break;
				}
			}
			name = URLDecoder.decode(name, "utf-8");
			String sql = "";
			if(null==directRole)
				sql = "select u.name as 'immediateBoss' from simple_user u left join simple_user_roles ur on(u.id=ur.userId) left join simple_role r on(r.id=ur.roleId) where u.name like '%" + name + "%' and r.defaultRole in('5','6','4','2') ";
			else
				sql = "select u.name as 'immediateBoss' from simple_user u left join simple_user_roles ur on(u.id=ur.userId) left join simple_role r on(r.id=ur.roleId) where u.name like '%" + name + "%' and r.defaultRole='"+directRole+"' ";
			return this.resignationDao.listSQL(sql, ResignationForm.class, false);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean getcheck(String id) {
		String userName = WebContextUtil.getUserName();
		ResignationEntity entity = resignationDao.load(ResignationEntity.class, id);
		if(entity.getImmediateBoss().equals(userName))
			return true;
		if(entity.getState()>0&&fistAudit.equals(userName.toLowerCase()))
			return true;
		if(entity.getState()>1&&secondAudit.equals(userName.toLowerCase())&&entity.isFlag())
			return true;
		if(entity.getState()>2&&thirdAudit.equals(userName.toLowerCase())&&entity.isFlag())
			return true;
		return false;
	}

	@Override
	public ResignationForm getAuditById(String id) {
		try {
			ResignationEntity entity ;
			if(id.equals("")){
				String personId = WebContextUtil.getCurrentUser().getUser().getPersonId();
				entity = this.loadByPersonIdandState(personId, true);
			}else{
				entity = resignationDao.load(ResignationEntity.class, id);
			}
			ResignationForm form = new ResignationForm();
			BeanUtils.copyNotNullProperties(entity, form);
			return form;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Msg sendEmail(String title,String content,String[] str) {
		try {
			SendEmailUtil.sendMail(title, content, str);
			return new Msg(true,"已邮件通知");
		} catch (Exception e) {
			return new Msg(false,"邮件发送失败");
		}
	}

	@Override
	public boolean getexist() {
		String personId = WebContextUtil.getCurrentUser().getUser().getPersonId();
		if(null==this.loadByPersonIdandState(personId, true))
			return false;
		else
			return true;
	}

	@Override
	public Msg revocation(String id) {
		String personId = WebContextUtil.getCurrentUser().getUser().getPersonId();
		ResignationEntity entity = this.resignationDao.load(ResignationEntity.class, id);
		if(entity.getState()!=0)
			return new Msg(false,"审核中，不能撤销");
		if(entity.getPersonId().equals(personId)){
			entity.setValid(false);
			resignationDao.update(entity);
			return new Msg(true,"撤销成功");
		}
		return new Msg(false,"权限不足");
	}

}
