package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.HolidaysUsersEntity;
import com.rosense.module.system.entity.PersonEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IHolidaysUserService;
import com.rosense.module.system.web.form.ExtraworkapplysForm;
import com.rosense.module.system.web.form.HolidayApplysForm;
import com.rosense.module.system.web.form.HolidaysUserForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

import net.sf.json.JSONObject;

@Service("holidaysUserService")
@Transactional
public class HolidaysUserService extends BaseService implements IHolidaysUserService {
	private Logger logger = Logger.getLogger(RoleService.class);
	@Inject
	private IBaseDao<HolidaysUsersEntity> hDao;
	@Inject
	private IBaseDao<RoleEntity> rDao;
	@Inject
	private IBaseDao<UserEntity> uDao;
	@Inject
	private IBaseDao<PersonEntity> pDao;

	@Override
	public void update(HolidaysUserForm form) {
		// TODO Auto-generated method stub
		try {
			String id=form.getId();
				String sql=" from HolidaysUsersEntity h where h.id='"+id+"'";
				List<HolidaysUsersEntity> hue=this.hDao.list(sql);
				BeanUtils.copyNotNullProperties(form,hue.get(0));
				this.hDao.update(hue.get(0));
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新假期信息失败===>异常信息：", e);
		}
	}
	@Override
	public DataGrid get(HolidaysUserForm form) {
		// TODO Auto-generated method stub
		try {
			
 			List<HolidaysUserForm> forms = new ArrayList<HolidaysUserForm>();
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
			String sql = "select p.name,p.employmentDate,p.becomeStaffDate, h.* from holidays_user h ";
			sql += " left join simple_user u ON(h.id=u.holidaysId) ";
			sql += " left join simple_person p ON(u.personId=p.id) ";
			sql += " left join simple_org o ON(p.orgChildId=o.id) ";
			sql += "left join simple_user_roles ur ON(ur.userId=u.id) ";
			sql += "left join simple_role r ON(r.id=ur.roleId) where";
			if (defaultRoles.indexOf("0")>-1) {
				sql+="(1=1) ";
				defaultRoles += "or";
			}
			if (defaultRoles.indexOf("4")>-1) {
				if (defaultRoles.indexOf("or")>-1)
					sql += "or ";
				else
					defaultRoles += "or";
				sql += "(o.id in('"+ocid+"','"+pList.get(0).getOrgId()+"') and r.defaultRole='2') ";
			}
			if(defaultRoles.indexOf("3")>-1){
				if (defaultRoles.indexOf("or")>-1)
					sql += "or ";
				else
					defaultRoles += "or";
				sql+="(1=1) ";
			}
			if(defaultRoles.indexOf("2")>-1){
				if (defaultRoles.indexOf("or")>-1)
					sql += "or ";
				else
					defaultRoles += "or";
				sql+="(o.id='"+pList.get(0).getOrgId()+"' and r.defaultRole='1') ";
			}
			if(defaultRoles.indexOf("5")>-1){
				if (defaultRoles.indexOf("or")>-1)
					sql += "or ";
				else
					defaultRoles += "or";
				sql += "(r.defaultRole in ('4','6','2')) ";
			}
			if(defaultRoles.indexOf("6")>-1){
				if (defaultRoles.indexOf("or")>-1)
					sql += "or ";
				else
					defaultRoles += "or";
				sql += "(r.defaultRole in ('4','2')) ";
			}
			if(defaultRoles.indexOf("1")>-1){
				if (defaultRoles.indexOf("or")>-1)
					sql += "or ";
				else
					defaultRoles += "or";
				sql += "(1!=1) ";
			}
			sql += "or u.account='" + name + "' ";
			Pager<HolidaysUserForm> pager = this.hDao.findSQL(sql, alias, HolidaysUserForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (HolidaysUserForm pf : pager.getDataRows()) {
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
	

	//年度清零
	public void reset(){
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select u.* from simple_user u ";
		Pager<UserForm> pager = this.hDao.findSQL(sql, alias, UserForm.class, false);
		if (null != pager && !pager.getDataRows().isEmpty()) {
			for (UserForm pf : pager.getDataRows()) {
				final HolidaysUsersEntity hue=this.hDao.load(HolidaysUsersEntity.class,pf.getHolidaysId());
				if(hue.getTheremainingSiLingfalse()>5.5){
					hue.setLastyearsremainingSiLingfalse(5.5);
					hue.setTheremainingSiLingfalse(hue.getLastyearsremainingSiLingfalse()+hue.getThisyearshouldbeSiLingfalse());
				}else {
					hue.setLastyearsremainingSiLingfalse(hue.getTheremainingSiLingfalse());
					hue.setTheremainingSiLingfalse(hue.getLastyearsremainingSiLingfalse()+hue.getThisyearshouldbeSiLingfalse());
				}
				hue.setTheremainingannualleave(hue.getShouldhaveannualleave());
				hue.setLastyearsremainingpaidleave(hue.getTheremainingpaidleave());
				hue.setNotvalidonanannualbasis(0);
				hue.setThisyearsdaysworkovertime(0);
				this.hDao.update(hue);
				
			}
		}
	}
	
	//病假清零
		public void Sickleave(){
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select u.* from simple_user u ";
			Pager<UserForm> pager = this.hDao.findSQL(sql, alias, UserForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (UserForm pf : pager.getDataRows()) {
					final HolidaysUsersEntity hue=this.hDao.load(HolidaysUsersEntity.class,pf.getHolidaysId());
					hue.setThismonthhasbeenonmedicalleave(0);
					this.hDao.update(hue);
					
				}
			}
		}
		
	//调休	
		public void Paidleave(){
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select h.* from extraworkapplys e where h.extworkapplystatement=1";
			Pager<ExtraworkapplysForm> pager = this.hDao.findSQL(sql, alias, ExtraworkapplysForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (ExtraworkapplysForm pf : pager.getDataRows()) {
					UserEntity ue=this.uDao.load(UserEntity.class,pf.getUserid());
					final HolidaysUsersEntity hue=this.hDao.load(HolidaysUsersEntity.class,ue.getHolidaysId());
					String kai = DateUtils.dateToString(pf.getExtworkapplystartdata(), "yyyy-MM-dd HH:mm:ss");
					int year=Integer.parseInt(kai.substring(kai.length() -17, kai.length()-15))+1;
					String nian=String.valueOf(year);
					String jie = kai.replace(kai.substring(kai.length() - 17, kai.length()-15),nian);
					if(kai.equals(jie)){
						hue.setTheremainingpaidleave(hue.getTheremainingpaidleave()-Double.parseDouble(pf.getExtworkapplydays()));
					}
					this.hDao.update(hue);
					
				}
			}
		}
	
	//年假司龄假自增长
		public void growth() {
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql="select u.* from simple_user u ";
			Pager<UserForm> pager = this.hDao.findSQL(sql, alias, UserForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (UserForm pf : pager.getDataRows()) {
					final HolidaysUsersEntity hue=this.hDao.load(HolidaysUsersEntity.class,pf.getHolidaysId());
					final PersonEntity pe=this.pDao.load(PersonEntity.class, pf.getPersonId());
				    Calendar calendar =  Calendar.getInstance();  
				    calendar.setTime(pe.getEmploymentDate());
				  //显示天; 
				    int date = calendar.get(Calendar.DATE);
				 // 显示月份 (从0开始, 实际显示要加一)  
				    int month = calendar.get(Calendar.MONTH);  
				 // 显示年份  
				    int year = calendar.get(Calendar.YEAR);  
				    
				 // 重置 Calendar 显示当前时间  
				     
				    String str = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());  
				    calendar.setTime(new Date()); 
				    int date2 = calendar.get(Calendar.DATE);
				    int month2 = calendar.get(Calendar.MONTH);
				    int year2 = calendar.get(Calendar.YEAR);  
				    if(date<28){
				    if(month+1<month2+1&&date==date2){
				    	if(hue .getShouldhaveannualleave()<5){	
				    		hue.setAnnualLeave(hue.getAnnualLeave()+0.92);
				    		double a=hue.getAnnualLeave();
				    		String temp=Double.toString(a);
				    		int start=temp.indexOf(".");
					    	if(Integer.parseInt(temp.substring(start+1,start+2))>5){
					    		hue.setShouldhaveannualleave((int)hue.getAnnualLeave()+0.5);
					    	}else if(Integer.parseInt(temp.substring(start+1,start+2))<5){
					    		hue.setShouldhaveannualleave((int)hue.getAnnualLeave());
					    	}else if(Integer.parseInt(temp.substring(start+1,start+2))==5){
					    		hue.setShouldhaveannualleave(hue.getAnnualLeave());
					    	}else if(hue.getShouldhaveannualleave()>5) {
					    		hue.setShouldhaveannualleave(5);
					    		hue.setThisyearshouldbeSiLingfalse(0.5);
							}
				    	}else if(hue .getShouldhaveannualleave()==5&&hue.getThisyearshouldbeSiLingfalse()<6){
				    		hue.setAnnualLeave(hue.getAnnualLeave()+0.92);
				    		double a=hue.getAnnualLeave();
				    		String temp=Double.toString(a);
				    		int start=temp.indexOf(".");
				    		if(Integer.parseInt(temp.substring(start+1,start+2))>5){
					    		hue.setThisyearshouldbeSiLingfalse((int)hue.getAnnualLeave()+0.5);
					    	}else if(Integer.parseInt(temp.substring(start+1,start+2))<5){
					    		hue.setThisyearshouldbeSiLingfalse((int)hue.getAnnualLeave());
					    	}else if(Integer.parseInt(temp.substring(start+1,start+2))==5){
					    		hue.setThisyearshouldbeSiLingfalse(hue.getAnnualLeave());
					    	}else if(hue.getAnnualLeave()>11) {
					    		hue.setThisyearshouldbeSiLingfalse(6);
							}
						}else{
							hue.setShouldhaveannualleave(hue.getShouldhaveannualleave());
							hue.setThisyearshouldbeSiLingfalse(hue.getThisyearshouldbeSiLingfalse());
						}
				    }
				    }
				    hue.setCalculateLength(hue.getCalculateLength()+1);
				    hue.setResidueHoliday(hue.getShouldhaveannualleave()+hue.getThisyearshouldbeSiLingfalse());
				    if(hue.getCalculateLength()==356){
				    	pe.setWorkAge(pe.getWorkAge()+1);
				    	this.pDao.update(pe);
				    }
					this.hDao.update(hue);
					
				}
			}
		}
		
	@Override
	public Msg importFile(List<HolidaysUserForm> importList) {
		// TODO Auto-generated method stub
				try {
		            for (int i = 0; i < importList.size(); i++) {
		            	HolidaysUserForm hUser = importList.get(i);
		            	int equalsAccount = this.equlasVal("h.useraccount='" + hUser.getUseraccount() + "'");
		            	
		    			if (equalsAccount == 1){
		    				hUser.setId(this.getId(hUser.getUseraccount()));
		    				//return new Msg(false, "账号"+secUser.getAccount()+"已经存在");
		    				this.rDao.executeSQL("update simple_user u left join simple_person e on(u.personId=e.id) set e.becomeStaffDate=? where u.account=?", new Object[]{hUser.getBecomeStaffDate(),hUser.getUseraccount()});
		    				this.update(hUser);
		    			}else{
		    				 return new Msg(false, "无员工编号为："+hUser.getUseraccount()+"的员工  请核对信息！");
		    				}
		            }
		            return new Msg(true, "导入员工信息成功！");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("导入员工信息失败===>异常信息：", e);
					return new Msg(false, "导入员工信息失败！");
				}
	}

	private int equlasVal(String param) {
		String sql = "select h.* from holidays_user h where " + param;
		return this.hDao.countSQL(sql, false).intValue();
	}
	
	private String getId(String param) {
		String idString="";
		String sql = "select h.* from holidays_user h where h.useraccount='" + param + "'";
		List<HolidaysUserForm> list = this.hDao.listSQL(sql, HolidaysUserForm.class, false);
		if (null != list && list.size() > 0) {
			for (HolidaysUserForm e : list) {
				 idString=e.getId();
			}
		}
		return idString;
	}
	@Override
	public HolidaysUserForm select(String id) {
		// TODO Auto-generated method stub
		try {
			String sql = "select u.name,p.grade,o.name positionname,p.workAge, h.* from holidays_user h ";
			sql += " left join simple_user u ON(h.id=u.holidaysId) ";
			sql += " left join simple_person p ON(u.personId=p.id) ";
			sql += " left join simple_org o ON(p.orgChildId=o.id) ";
			sql+="where h.id=?";

			HolidaysUserForm holidaysUserForm = (HolidaysUserForm) this.hDao.queryObjectSQL(sql, new Object[] { id },
					HolidaysUserForm.class, false);
			Date dateTime = DateUtils.getDate(DateUtils.getSysDateStr(),"date");
			Calendar calendar =  Calendar.getInstance();  
		    calendar.setTime(dateTime);
		 // 显示月份 (从0开始, 实际显示要加一)  
		    int month = calendar.get(Calendar.MONTH);  
		 // 显示年份  
		    int year = calendar.get(Calendar.YEAR); 
		    String years=year+"年"+"/"+(month+1)+"月";
			holidaysUserForm.setMonth(years);
			holidaysUserForm.setHaveToTakePaidLeave(holidaysUserForm.getThisyearsdaysworkovertime()-holidaysUserForm.getTheremainingpaidleave());
			return holidaysUserForm;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载假期信息失败===>异常信息：", e);
			throw new ServiceException("加载信息异常：", e);
		}
	}
	
	/**
	 * 获取当前用户角色编号
	 */
	public int getCurrentUserDefaultRole(){
		List<Integer> roleId=new ArrayList<Integer>();
		List<String> roleName=new ArrayList<String>();
		//判断当前用户是否具有多个角色
		if(WebContextUtil.getCurrentUser().getUser().getRole_ids().contains(",")){
			String[] roleIds=WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
			for(String id:roleIds){
				RoleForm role=(RoleForm) this.rDao.queryObjectSQL("select * from simple_role where id=?",new Object[]{id},RoleForm.class,false);
				if(role!=null){
					roleId.add(role.getDefaultRole());
					roleName.add(role.getSn());
				}
				
			}
			
		}else{
			RoleEntity role=this.rDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
			roleId.add(role.getDefaultRole());
			roleName.add(role.getSn());
		}
		 //超级管理员
		 if(roleId.contains(0)){
			 return 0;
		 }else if(roleId.contains(4)){
			 //部门总监
			 return 4;
		 }else if(roleId.contains(4)&&roleId.contains(3)){
			 //部门总监兼HR角色
			 return 43;
		 }else if(roleId.contains(2)&&roleId.contains(3)){
			 //部门主管兼HR角色
			 return 23;
		 }else if(roleId.contains(2)){
			 //部门主管
			 return 2;
		 }else if(roleId.contains(3)&&roleId.contains(1)){
			 //HR以及普通用户
			 return 13;
		 }else if(roleId.contains(3)){
			 //HR
			 return 3;
		 }else{
			
		 }
		return 1;
	}
	
	/**
	 * 判断用户账号是否存在
	 */
	public int equlasValAccount(String account) {
		String sql = "select u.* from simple_user u where u.account='" + account + "'";
		return this.uDao.countSQL(sql, false).intValue();
	}
}
