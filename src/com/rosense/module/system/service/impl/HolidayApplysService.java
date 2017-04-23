package com.rosense.module.system.service.impl;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.HolidayApplysEntity;
import com.rosense.module.system.entity.HolidaysUsersEntity;
import com.rosense.module.system.entity.PersonEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IHolidayApplysService;
import com.rosense.module.system.web.form.HolidayApplysForm;
import com.rosense.module.system.web.form.UserForm;
import net.sf.json.JSONObject;

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
	@Inject
	private IBaseDao<HolidaysUsersEntity> huDao;

	@Override
	public Msg add(HolidayApplysForm form) {
		// TODO Auto-generated method stub
		try {
			String hql="from UserEntity u where u.id='"+WebContextUtil.getCurrentUser().getUser().getUserId()+"'";
			List<UserEntity> uList=this.uDao.list(hql);
			String hql2="from HolidaysUsersEntity h where h.id='"+uList.get(0).getHolidaysId()+"'";
			List<HolidaysUsersEntity> hList=this.huDao.list(hql2);
			form.setApplyForTime(DateUtils.getSysDateStr());
			
			Date startDate = DateUtils.getDate(form.getHoliapplyStartDate(), "date");
			Date endDate = DateUtils.getDate(form.getHoliapplyEndDate(), "date");
			if(null==startDate||startDate.toString().equals(""))
				return new Msg(false,"请假日期有误");
			if(null==endDate||endDate.toString().equals(""))
				return new Msg(false,"请假日期有误");	
			if(Double.parseDouble(form.getHoliapplyDays())<=0)
				return new Msg(false,"请假日期有误");
			double days = DateUtils.daysBetween(startDate,endDate) - 1;
			if(Double.parseDouble(form.getStartHours())==9)
				days+=1;
			else if(Double.parseDouble(form.getStartHours())==14)
				days+=0.5;
			if(Double.parseDouble(form.getEndHours())==14)
				days+=0.5;
			else if(Double.parseDouble(form.getEndHours())==18)
				days+=1;
			if(days!=Double.parseDouble(form.getHoliapplyDays())){
				logger.error("申请假期失败===>请假天数有误：");
				return null;
			}
			if (form.getHoliapplyName().equals("病假")) {
				if (hList.get(0).getThismonthhasbeenonmedicalleave() == 2) {
					return new Msg(false, "本月病假已用完！");
				} else if (Double.parseDouble(form.getHoliapplyDays()) > 2) {
					return new Msg(false, "病假不能超过两天！");
				}
			} else if (form.getHoliapplyName().equals("调休")) {
				if (hList.get(0).getTheremainingpaidleave() == 0) {
					return new Msg(false, "调休假已用完!");
				} else if (hList.get(0).getTheremainingpaidleave() < Double.parseDouble(form.getHoliapplyDays())) {
					return new Msg(false, "调休假天数不足！");
				}
			}else if(form.getHoliapplyName().equals("司龄假")){
				if(hList.get(0).getLastyearsremainingSiLingfalse()==0){
					if(hList.get(0).getThisyearshouldbeSiLingfalse()<Double.parseDouble(form.getHoliapplyDays())){
						return new Msg(false, "司龄假天数不足！");
					}else if(hList.get(0).getThisyearshouldbeSiLingfalse()==0){
						return new Msg(false, "司龄假已用完!");
					}
				}
			}else if(form.getHoliapplyName().equals("年假")){
				if(hList.get(0).getTheremainingannualleave()==0){
					return new Msg(false, "年假已用完!");
				}else if(hList.get(0).getTheremainingannualleave()<Double.parseDouble(form.getHoliapplyDays())){
					return new Msg(false, "年假天数不足!");
				}
			}
			if (DateUtils.isWeekend(form.getHoliapplyStartDate())) {
				return new Msg(false, "假期开始日为周末");
			}
			final HolidayApplysEntity p = new HolidayApplysEntity();
			if(form.getHoliapplyUserName()==null){
				form.setHoliapplyUserName(WebContextUtil.getCurrentUser().getUser().getName());
			}
			form.setHoliapplyUserName(WebContextUtil.getCurrentUser().getUser().getName());
			form.setUid(WebContextUtil.getCurrentUser().getUser().getUserId());
			BeanUtils.copyNotNullProperties(form, p);
			this.hDao.add(p);
			this.logService.add("申请假期", "账号：[" + form.getHoliapplyUserName() + "]");

			//sendMail("请假申请", form.getHoliapplyUserName()+"申请休假，请及时审批！",getemail(WebContextUtil.getCurrentUser().getUser().getOrgId()).getEmail());

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
//			if ("admin".equals(WebContextUtil.getCurrentUser().getUser().getAccount())) {
//				h.setHoliapplystatement(3);
//				this.hDao.update(h);
//				this.logService.add("撤销假期", "账号：[" + h.getHoliapplyUserName() + "]");
//				return new Msg(true, "撤销成功！");
//			}
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
		// TODO Auto-generated method stub
		try {
			HolidayApplysEntity hae= this.hDao.load(HolidayApplysEntity.class,form.getId());
			if(hae.getHoliapplystatement()==1||hae.getHoliapplyhrapproval()==1)
				return new Msg(false,"正在审核");
			String hql="from UserEntity u where u.id='"+WebContextUtil.getCurrentUser().getUser().getUserId()+"'";
			List<UserEntity> uList=this.uDao.list(hql);
			String hql2="from HolidaysUsersEntity h where h.id='"+uList.get(0).getHolidaysId()+"'";
			List<HolidaysUsersEntity> hList=this.huDao.list(hql2);
			Date startDate = DateUtils.getDate(form.getHoliapplyStartDate(), "date");
			Date endDate = DateUtils.getDate(form.getHoliapplyEndDate(), "date");
			if(null==startDate||startDate.toString().equals(""))
				return new Msg(false,"请假日期有误");
			if(null==endDate||endDate.toString().equals(""))
				return new Msg(false,"请假日期有误");	
			if(Double.parseDouble(form.getHoliapplyDays())<=0)
				return new Msg(false,"请假日期有误");
			double days = DateUtils.daysBetween(startDate,endDate) - 1;
			if(Double.parseDouble(form.getStartHours())==9)
				days+=1;
			else if(Double.parseDouble(form.getStartHours())==14)
				days+=0.5;
			if(Double.parseDouble(form.getEndHours())==14)
				days+=0.5;
			else if(Double.parseDouble(form.getEndHours())==18)
				days+=1;
			if(days!=Double.parseDouble(form.getHoliapplyDays())){
				logger.error("申请假期失败===>请假天数有误：");
				return null;
			}
			if (form.getHoliapplyName().equals("病假")) {
				if (hList.get(0).getThismonthhasbeenonmedicalleave() == 2) {
					return new Msg(false, "本月病假已用完！");
				} else if (Double.parseDouble(form.getHoliapplyDays()) > 2) {
					return new Msg(false, "病假不能超过两天！");
				}
			} else if (form.getHoliapplyName().equals("调休")) {
				if (hList.get(0).getTheremainingpaidleave() == 0) {
					return new Msg(false, "调休假已用完!");
				} else if (hList.get(0).getTheremainingpaidleave() < Double.parseDouble(form.getHoliapplyDays())) {
					return new Msg(false, "调休假天数不足！");
				}
			}else if(form.getHoliapplyName().equals("司龄假")){
				if(hList.get(0).getLastyearsremainingSiLingfalse()==0){
					if(hList.get(0).getThisyearshouldbeSiLingfalse()<Double.parseDouble(form.getHoliapplyDays())){
						return new Msg(false, "司龄假天数不足！");
					}else if(hList.get(0).getThisyearshouldbeSiLingfalse()==0){
						return new Msg(false, "司龄假已用完!");
					}
				}
			}else if(form.getHoliapplyName().equals("年假")){
				if(hList.get(0).getTheremainingannualleave()==0){
					return new Msg(false, "年假已用完!");
				}else if(hList.get(0).getTheremainingannualleave()<Double.parseDouble(form.getHoliapplyDays())){
					return new Msg(false, "年假天数不足!");
				}
			}
			if (DateUtils.isWeekend(form.getHoliapplyStartDate())) {
				return new Msg(false, "假期开始日为周末");
			}
			hae.setHoliapplyContent(form.getHoliapplyContent());
			hae.setHoliapplyDays(form.getHoliapplyDays());
			hae.setHoliapplyStartDate(form.getHoliapplyStartDate());
			hae.setHoliapplyEndDate(form.getHoliapplyEndDate());
			hae.setEnclosure(form.getEnclosure());
			hae.setEnclosuretwo(form.getEnclosuretwo());
			hae.setHoliapplyremark(form.getHoliapplyremark());
			hae.setHoliapplydirectorsapproval(0);
			hae.setHoliapplyhrapproval(0);
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
			String name = WebContextUtil.getCurrentUser().getUser().getAccount();
			String[] ids = WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
			String defaultRoles = "";
			for (int i = 0; i < ids.length; i++) {
				defaultRoles += this.rDao.load(RoleEntity.class, ids[i]).getDefaultRole();
			}
			String hql2="from PersonEntity p where p.id='"+WebContextUtil.getCurrentUser().getUser().getPersonId()+"'";
			List<PersonEntity> pList=this.pDao.list(hql2);
			String ocid= pList.get(0).getOrgChildId();
			String sql = "select distinct(o.name) positionname,p.chinaname,p.name englishName,h.* ";
			sql += "from holidayapplys h left join simple_user u ON(h.uid=u.id) ";
			sql += "left join simple_person p ON(u.personId=p.id) ";
			sql += "left join simple_org o ON(p.orgChildId=o.id) ";
			sql += "left join simple_user_roles ur on(ur.userId=u.id) ";
			sql += "left join simple_role r on(ur.roleId=r.id) ";
			sql += "where ";
			String condiction = "";
			if (defaultRoles.indexOf("0")>-1) {
				condiction += "(h.holiapplystatement!='3' or u.id='"+WebContextUtil.getUserId()+"') ";
				defaultRoles += "or";
			}
			if (defaultRoles.indexOf("4")>-1) {//总监
				if (defaultRoles.indexOf("or")>-1)
					condiction += "or ";
				else
					defaultRoles += "or";
				condiction += "(u.account = '"+name+"' or u.id='"+WebContextUtil.getUserId()+"') ";
			}
			if (defaultRoles.indexOf("6")>-1) {//经理
				if (defaultRoles.indexOf("or")>-1)
					condiction += "or ";
				else
					defaultRoles += "or";
				condiction += "(u.account = '"+name+"' or u.id='"+WebContextUtil.getUserId()+"') ";
			}
			if (defaultRoles.indexOf("3")>-1) {//HR
				if (defaultRoles.indexOf("or")>-1)
					condiction += "or ";
				else
					defaultRoles += "or";
				condiction += "(h.holiapplydirectorsapproval=1 or u.id='"+WebContextUtil.getUserId()+"') ";
			}
			if (defaultRoles.indexOf("2")>-1) {//主管
				if (defaultRoles.indexOf("or")>-1)
					condiction += "or ";
				else
					defaultRoles += "or";
				condiction += "((o.id='"+WebContextUtil.getCurrentUser().getUser().getOrgChildId()+"' and h.holiapplydirectorsapproval='0') or u.id='"+WebContextUtil.getUserId()+"') ";
			}
			if (defaultRoles.indexOf("5")>-1) {//总经理
				if (defaultRoles.indexOf("or")>-1)
					condiction += "or ";
				else
					defaultRoles += "or";
				condiction += "((r.defaultRole in ('4','6','2') and h.holiapplydirectorsapproval='0') or u.id='"+WebContextUtil.getUserId()+"') ";
			}
			if (defaultRoles.indexOf("1")>-1) {//普通用户
				if (defaultRoles.indexOf("or")>-1)
					condiction += "or ";
				condiction += "(u.account='" + name + "' or u.id='"+WebContextUtil.getUserId()+"') ";
			}
			if(defaultRoles.length()>1)
				condiction = "("+condiction+")";
			sql += condiction + "and h.holiapplystatement!='3' ";
			if(null!=form.getSort()){
				SystemContext.setSort("h."+form.getSort());
				SystemContext.setOrder(form.getOrder());
			}else{
				SystemContext.setSort("h.holiapplyStartDate desc,h.holiapplystatement ");
				SystemContext.setOrder("desc");
			}
			if(null!=form.getSearch()){
				sql += "and p.name like '%%"+form.getSearch()+"%%' ";
			}
			Pager<HolidayApplysForm> pager = this.hDao.findSQL(sql, alias, HolidayApplysForm.class, false);
			if(defaultRoles.length()>1){
				String e = sql.substring(sql.indexOf("from"));
				String c = "select count(distinct h.id) " + e;
				BigInteger total = (BigInteger)this.hDao.getCurrentSession().createSQLQuery(c).uniqueResult();
				pager.setTotal( total.longValue());
			}
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
			logger.error("加载假期列表信息失败===>异常信息：", e);
			throw new ServiceException("加载假期列表信息异常：", e);
		}
	}

	@Override
	public HolidayApplysForm get(String id) {
		// TODO Auto-generated method stub
		try {
			if(null!=id&&!id.trim().equals("")){
				String sql = "select o.name positionname,h.* from holidayapplys h ";
				sql += " left join simple_user u ON(h.uid=u.id) ";
				sql += " left join simple_person p ON(u.personId=p.id) ";
				sql += " left join simple_org o ON(p.orgId=o.id) ";
				sql += " where h.id=?";
				
				HolidayApplysForm form = (HolidayApplysForm) this.hDao.queryObjectSQL(sql, new Object[] { id },
						HolidayApplysForm.class, false);
				return form;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载假期申请信息失败===>异常信息：", e);
			throw new ServiceException("加载用户信息异常：", e);
		}
	}

	@Override
	public Msg approvaltrue(HolidayApplysForm form) {
		try {
			HolidayApplysEntity h = this.hDao.load(HolidayApplysEntity.class, form.getId());
			UserEntity u = this.uDao.load(UserEntity.class, h.getUid());
			PersonEntity p = this.pDao.load(PersonEntity.class, u.getPersonId());
			HolidaysUsersEntity hu = this.huDao.load(HolidaysUsersEntity.class, u.getHolidaysId());
//			if ("admin".equals(WebContextUtil.getCurrentUser().getUser().getAccount())) {
//				if(form.getPr().equals("1")){
//					h.setHoliapplydirectorsapproval(1);
//					h.setHoliapplyhrapproval(1);
//					h.setHoliapplystatement(1);
//				}else if(form.getPr().equals("2")){
//					h.setHoliapplydirectorsapproval(2);
//					h.setHoliapplyhrapproval(2);
//					h.setHoliapplystatement(2);
//				}
//				h.setDirector(WebContextUtil.getCurrentUser().getUser().getName());
//				h.setApprovalTime(DateUtils.getSysDateStr());
//				h.setHoliapplydirectorsopinion(form.getHoliapplydirectorsopinion());
//				h.setHoliapplyhropinion(form.getHoliapplyhropinion());
//				this.logService.add("假期审批", "账号：[" + WebContextUtil.getCurrentUser().getUser().getAccount() + "]");
//				this.hDao.update(h);
//				return new Msg(true, "假期已审批");
//			}
			if (h.getHoliapplydirectorsapproval()==0) {
				if(form.getPr().equals("1")){
					h.setHoliapplydirectorsapproval(1);
				}else if(form.getPr().equals("2")){
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
			if (h.getHoliapplydirectorsapproval()==1&&h.getHoliapplyhrapproval()==0) {
				if(form.getPr().equals("1")){
					h.setHoliapplyhrapproval(1);
					h.setHoliapplystatement(1);
				}else if(form.getPr().equals("2")){
					h.setHoliapplyhrapproval(2);
					h.setHoliapplystatement(2);
				}
				h.setHoliapplyhropinion(form.getHoliapplyhropinion());
				this.logService.add("假期审批", "账号：[" + WebContextUtil.getCurrentUser().getUser().getAccount() + "]");
				this.hDao.update(h);
			//	sendMail("审核通知", "已通过HR审批", p.getEmail());
				// 更新假期列表
				if (h.getHoliapplyName().equals("病假")) {
					double bing=hu.getThismonthhasbeenonmedicalleave() + Double.parseDouble(h.getHoliapplyDays());
					hu.setThismonthhasbeenonmedicalleave(bing);
					hu.setResidueHoliday(hu.getResidueHoliday()-Double.parseDouble(h.getHoliapplyDays()));
				} else if (h.getHoliapplyName().equals("事假")) {
					double shi=hu.getNotvalidonanannualbasis() + Double.parseDouble(h.getHoliapplyDays());
					hu.setNotvalidonanannualbasis(shi);
					hu.setResidueHoliday(hu.getResidueHoliday()-Double.parseDouble(h.getHoliapplyDays()));
				} else if (h.getHoliapplyName().equals("年假")) {
					double nian=hu.getTheremainingannualleave() - Double.parseDouble(h.getHoliapplyDays());
					hu.setTheremainingannualleave(nian);
					hu.setAlreadyAnnualLeave(hu.getAlreadyAnnualLeave()+Double.parseDouble(h.getHoliapplyDays()));
					hu.setResidueHoliday(hu.getResidueHoliday()-Double.parseDouble(h.getHoliapplyDays()));
				} else if (h.getHoliapplyName().equals("司龄假")) {
					double si=hu.getTheremainingSiLingfalse() - Double.parseDouble(h.getHoliapplyDays());
					hu.setTheremainingSiLingfalse(si);
					hu.setAlreadySiLingFalse(hu.getAlreadySiLingFalse()+Double.parseDouble(h.getHoliapplyDays()));
					hu.setResidueHoliday(hu.getResidueHoliday()-Double.parseDouble(h.getHoliapplyDays()));
				} else if (h.getHoliapplyName().equals("调休")) {
					double tiao=hu.getTheremainingpaidleave() - Double.parseDouble(h.getHoliapplyDays());
					hu.setTheremainingpaidleave(tiao);
					hu.setResidueHoliday(hu.getResidueHoliday()-Double.parseDouble(h.getHoliapplyDays()));
				}
				this.huDao.update(hu);
				return new Msg(true, "假期已审批");
			}
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"请联系管理员");
		}
		return new Msg(false,"审核失败");
}




public UserForm  getemail(String id){
	try {
		String sql = "select u.*,e.chinaname,e.email,e.sex,e.employmentStr,e.phone,e.job,e.province, e.orgId, e.positionId, e.becomeStaffDate,e.birthday,e.securityDate,e.school,e.profession,e.graduation, e.degree,e.accountAddr,e.accountPro,e.address,e.age,e.workAge,e.probationLimit,e.probationEnd,e.marriage,e.agreementEndDate,e.agreementLimit,e.positionEng,e.orgChildId,e.agreementStartDate,e.agreementTimes,e.area,e.workOld,e.material,e.bankCard,e.bear,e.idcard,e.nation,e.origin,e.train,e.securityCard,e.politicalFace,e.certificate,e.contact,e.contactPhone,e.fund,e.fundDate,o.name orgName,p.name positionName from simple_user u ";
		sql += " left join simple_person e ON(u.personId=e.id) ";
		sql += " left join simple_org o ON(e.orgId=o.id)  ";
		sql += " left join simple_position p ON(e.positionId=p.id)  ";
		sql += " left join simple_user_roles ur ON(u.id=ur.userId)  ";
		sql += " left join simple_role r ON(ur.roleId=r.id)  ";
		sql += " where  r.defaultRole=2 and e.orgId=?";

		UserForm form = (UserForm) this.uDao.queryObjectSQL(sql, new Object[] { id }, UserForm.class, false);	

		return form;
	} catch (Exception e) {
		e.printStackTrace();
		logger.error("获取信息失败===>异常信息：", e);
		throw new ServiceException("获取信息异常：", e);
	}

}

@Override
public HolidayApplysForm loading(String id) {
	// TODO Auto-generated method stub
	try {
		String sql = "select o.name positionname,u.name holiapplyUserName from simple_user u ";
		sql += " left join simple_person p ON(u.personId=p.id) ";
		sql += " left join simple_org o ON(p.orgChildId=o.id) ";
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
			return new Msg(false,"已经审核");
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
}
