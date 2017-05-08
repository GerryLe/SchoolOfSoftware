package com.rosense.module.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.HolidayApplysEntity;
import com.rosense.module.system.entity.PersonEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IHolidayApplysService;
import com.rosense.module.system.web.form.HolidayApplysForm;
import com.rosense.module.system.web.form.UserForm;

@Service("holidayApplysService")
@Transactional
public class HolidayApplysService extends BaseService implements IHolidayApplysService {
	private static final String FORMAT_DATE = null;
	private Logger logger = Logger.getLogger(RoleService.class);
	@Inject
	private IBaseDao<HolidayApplysEntity> hDao;
	@Inject
	private IBaseDao<RoleEntity> rDao;
	@Inject
	private IBaseDao<PersonEntity> pDao;
	@Inject
	private IBaseDao<UserEntity> uDao;

	@Override
	public Msg add(HolidayApplysForm form) {
		try {
			String hql="from UserEntity u where u.id='"+WebContextUtil.getCurrentUser().getUser().getUserId()+"'";
			List<UserEntity> uList=this.uDao.list(hql);
			form.setApplyForTime(DateUtils.getSysDateStr());
			Date startDate = DateUtils.getDate(form.getHoliapplyStartDate(), "date");
			Date endDate = DateUtils.getDate(form.getHoliapplyEndDate(), "date");
			if(null==startDate||startDate.toString().equals(""))
				return new Msg(false,"请假日期有误");
			if(null==endDate||endDate.toString().equals(""))
				return new Msg(false,"请假日期有误");	
			if(Double.parseDouble(form.getHoliapplyDays())<=0)
				return new Msg(false,"请假日期有误");
			/*if (DateUtils.isWeekend(form.getHoliapplyStartDate())) {
				return new Msg(false, "假期开始日为周末");
			}*/
			final HolidayApplysEntity p = new HolidayApplysEntity();
			if(form.getHoliapplyUserName()==null){
				form.setHoliapplyUserName(WebContextUtil.getCurrentUser().getUser().getName());
			}
			form.setUid(WebContextUtil.getCurrentUser().getUser().getUserId());
			BeanUtils.copyNotNullProperties(form, p);
			this.hDao.add(p);
			this.logService.add("申请假期", "账号：[" + form.getHoliapplyUserName() + "]");
            this.logService.add("申请假期", "账号：[" + form.getHoliapplyUserName() + "]");
			return new Msg(true, "申请成功！");
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("申请假期失败===>异常信息：", e);
			return new Msg(false, "申请失败！");
		}
	}

	@Override
	public Msg delete(String id) {
		try {
			HolidayApplysEntity h = this.hDao.load(HolidayApplysEntity.class, id);
			if (!h.getUid().equals(WebContextUtil.getUserId())) {
				return new Msg(false, "权限不足!");
			}
			if (h.getHoliapplystatement() == 0) {
				h.setHoliapplystatement(3);
				this.hDao.update(h);
				this.logService.add("撤销假期", "账号：[" + h.getHoliapplyUserName() + "]");
				return new Msg(true, "撤销成功！");
			} else {
				this.logService.add("撤销假期", "账号：[" + h.getHoliapplyUserName() + "]");
				return new Msg(false, "已审批，不能撤销！");
			}
		} catch (Exception e) {
			logger.error("撤销假期失败===>异常信息：", e);
			return new Msg(false, "撤销失败！");
		}
	}

	@Override
	public Msg update(HolidayApplysForm form) {
		try {
			HolidayApplysEntity hae= this.hDao.load(HolidayApplysEntity.class,form.getId());
			if(hae.getHoliapplystatement()==3)
				return new Msg(false,"请假信息已经注销");
			if(hae.getHoliapplystatement()!=0)
				return new Msg(false,"请假已经审批");
			Date startDate = DateUtils.getDate(form.getHoliapplyStartDate(), "date");
			Date endDate = DateUtils.getDate(form.getHoliapplyEndDate(), "date");
			if(null==startDate||startDate.toString().equals(""))
				return new Msg(false,"请假日期有误");
			if(null==endDate||endDate.toString().equals(""))
				return new Msg(false,"请假日期有误");	
			if(Double.parseDouble(form.getHoliapplyDays())<=0)
				return new Msg(false,"请假日期有误");
			BeanUtils.copyNotNullProperties(form,hae);
			hae.setApplyForTime(DateUtils.formatYYYYMMDD(new Date()));
			hae.setHoliapplydirectorsapproval(0);
			hae.setHoliapplystatement(0);
			this.hDao.update(hae);
			this.logService.add("修改假期", "名称：[" + form.getHoliapplyUserName() + "]");
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			logger.error("修改假期信息失败===>异常信息：", e);
			return new Msg(false, "修改假期信息失败！");

		}
	}

	@Override
	public DataGrid holidayApplysdg(HolidayApplysForm form) {
		try {
			List<HolidayApplysForm> forms = new ArrayList<HolidayApplysForm>();
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select u.account ,h.* ";
			//辅导员获取请假申请信息
			if(WebContextUtil.getCurrentUser().getUser().getDefaultRole()==1){
				sql += "from holidayapplys h left join simple_user u ON(h.uid=u.id) ";
				sql+="where h.holiapplystatement=0";
                sql += " or u.id='"+WebContextUtil.getUserId()+"'";
			}else if(WebContextUtil.getCurrentUser().getUser().getDefaultRole()==4){//学生获取请假申请信息
				sql += "from holidayapplys h left join simple_user u ON(h.uid=u.id) ";
				sql += "left join simple_student e ON(u.personId=e.id) ";
				sql += "where u.id='"+WebContextUtil.getUserId()+"'";
			}else if(WebContextUtil.getCurrentUser().getUser().getDefaultRole()==3){//教师获取请假申请信息
				sql += "from holidayapplys h left join simple_user u ON(h.uid=u.id) ";
				sql += "left join simple_teacher e ON(u.personId=e.id) ";
				sql += "where u.id='"+WebContextUtil.getUserId()+"'";
			}else{
				sql += "from holidayapplys h where 1=1";
			}
			Pager<HolidayApplysForm> pager = this.hDao.findSQL(sql, alias, HolidayApplysForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (HolidayApplysForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载请假列表信息失败===>异常信息：", e);
			throw new ServiceException("加载请假列表信息异常：", e);
		}
	}

	@Override
	public HolidayApplysForm get(String id) {
		try {
			if(null!=id&&!id.trim().equals("")){
				String sql = "select h.* from holidayapplys h ";
				sql += " where h.id=?";
				HolidayApplysForm form = (HolidayApplysForm) this.hDao.queryObjectSQL(sql, new Object[] { id },
						HolidayApplysForm.class, false);
				return form;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载申请信息失败===>异常信息：", e);
			throw new ServiceException("加载用户信息异常：", e);
		}
	}

	@Override
	public Msg approvaltrue(HolidayApplysForm form) {
		try {
			HolidayApplysEntity h = this.hDao.load(HolidayApplysEntity.class, form.getId());
			UserEntity u = this.uDao.load(UserEntity.class, h.getUid());
			if(h.getHoliapplystatement()==3){
				return new Msg(true, "请假已撤销");
			}
			if(h.getHoliapplystatement()!=0){
				return new Msg(true, "请假已审批");
			}
			else{
				if(form.getPr().equals("pass")){
					h.setHoliapplydirectorsapproval(1);
					h.setHoliapplystatement(1);
				}else{
					h.setHoliapplydirectorsapproval(2);
					h.setHoliapplystatement(2);
				}
				h.setDirector(WebContextUtil.getCurrentUser().getUser().getName());
				h.setApprovalTime(DateUtils.getSysDateStr());
				h.setHoliapplydirectorsopinion(form.getHoliapplydirectorsopinion());
				this.logService.add("假期审批", "账号：[" + WebContextUtil.getCurrentUser().getUser().getAccount() + "]");
				this.hDao.update(h);
				return new Msg(true, "假期已审批");
			}
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"请联系管理员");
		}
}

@Override
public HolidayApplysForm loading(String id) {
	try {
		String sql = "select c.class_name class_name,u.name holiapplyUserName from simple_user u ";
		if(WebContextUtil.getCurrentUser().getUser().getDefaultRole()==4){
			sql += " left join simple_student e ON(u.personId=e.id) ";
		}else{
			sql += " left join simple_teacher e ON(u.personId=e.id) ";
		}
		sql += " left join simple_class c ON(e.class_id=c.id) ";
		sql += " where u.id=?";

		HolidayApplysForm form = (HolidayApplysForm) this.hDao.queryObjectSQL(sql, new Object[] { id },HolidayApplysForm.class, false);
		form.setApplyForTime(DateUtils.getSysDateStr());
		return form;
	} catch (Exception e) {
		e.printStackTrace();
		logger.error("加载假期申请信息失败===>异常信息：", e);
		throw new ServiceException("加载用户信息异常：", e);
	}
}

@Override
public Msg getLimit(String id) {
	HolidayApplysEntity h = this.hDao.load(HolidayApplysEntity.class, id);
	if(WebContextUtil.getUserId().equals(h.getUid())){
		if(h.getHoliapplystatement()==0){
			if(h.getHoliapplydirectorsapproval()==0)
				return new Msg(true);
			return new Msg(false,"正在审核");
		}else if(h.getHoliapplystatement()==1){
			return new Msg(false,"审核已通过");
		}else if(h.getHoliapplystatement()==2){
			return new Msg(true);
		}else{
			return new Msg(false,"已经撤销");
		}
	}else{
		return new Msg(false,"权限不足");
	}
}

@Override
public Msg getaudit(String id) {
	//获取审批权限
	if(WebContextUtil.getCurrentUser().getUser().getApproveAuth().equals("1")){
		String[] ids = WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
		String defaultRoles = "";
		for (int i = 0; i < ids.length; i++) {
			defaultRoles += this.rDao.load(RoleEntity.class, ids[i]).getDefaultRole();
		}
		HolidayApplysEntity h = this.hDao.load(HolidayApplysEntity.class, id);
		if(WebContextUtil.getUserId().equals(h.getUid())){
			if(defaultRoles.indexOf("3")>-1&&defaultRoles.indexOf("2")>-1&&h.getHoliapplydirectorsapproval()==1&&h.getHoliapplyhrapproval()==0)
				return new Msg(true);
			else if(defaultRoles.indexOf("3")>-1&&h.getHoliapplydirectorsapproval()==1&&h.getHoliapplyhrapproval()==0)
				return new Msg(true);
			return new Msg(false,"权限不足");
		}
		if(h.getHoliapplystatement()==0){
			if(h.getHoliapplydirectorsapproval()==0){
				if(defaultRoles.indexOf("2")>-1||defaultRoles.indexOf("5")>-1||defaultRoles.indexOf("0")>-1)
					return new Msg(true);
				else
					return new Msg(false,"权限不足");
			}else if(h.getHoliapplydirectorsapproval()==1&&h.getHoliapplyhrapproval()==0){
				if(defaultRoles.indexOf("3")>-1||defaultRoles.indexOf("0")>-1)
					return new Msg(true);
				else 
					return new Msg(false,"权限不足");
			}
		}
		return new Msg(false,"已审核");
	}else{
		return new Msg(false,"权限不足");
	}
}

@Override
public List<UserForm> getDirector(String name) {
	 String sqlDirector="select u.* from simple_role r left join simple_user_roles ur on(ur.roleId=r.id)  left join simple_user u on(ur.userId=u.id)";
	 sqlDirector+="  where r.defaultRole in(1)"; 
	return this.hDao.listSQL(sqlDirector, UserForm.class, false);
}

}
