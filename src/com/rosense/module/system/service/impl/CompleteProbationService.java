package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.xml.ws.WebEndpoint;

import org.apache.tools.ant.taskdefs.SendEmail;
import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.codehaus.jackson.map.util.BeanUtil;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.SendEmailUtil;
import com.rosense.basic.util.StringUtil;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.OrgEntity;
import com.rosense.module.system.entity.PersonEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.ICompleteProbationService;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

import net.sf.json.JSONObject;

@Service("completeProbationService")
@Transactional
public class CompleteProbationService implements ICompleteProbationService {

	
	@Inject
	private IBaseDao<PersonEntity> personDao;
	@Inject
	private IBaseDao<RoleEntity> roleDao;
	@Inject
	private IBaseDao<OrgEntity> orgDao;
	@Inject
	private IBaseDao<UserEntity> userDao;
	
	@Override
	public DataGrid select(UserForm form) {
		try{

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
			if (null == form.getBecomeStaffDate()) {
				SystemContext.setSort("e.becomeStaffDate");
				SystemContext.setOrder("asc");
			}
			List<UserForm> forms=new ArrayList<UserForm>();
			Map<String, Object> alias=new HashMap<String,Object>();
			//RoleEntity role=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
			String cursql="select e.* from simple_person e left join simple_org o on(e.orgChildId=o.id) where e.name='"+ WebContextUtil.getCurrentUser().getUser().getName()+"'";
			UserForm curForm=(UserForm) this.userDao.queryObjectSQL(cursql, UserForm.class,false);
			if(curForm!=null){
				String sql = "select u.*,e.orgId,e.orgChildId,e.hrNotice,e.orgChargeApprove,e.orgHeadApprove,e.approveStatus,e.orgChargeName,e.orgChargeResult,e.orgHeadName,e.orgHeadResult,e.orgChargeRemark,e.orgHeadRemark,e.chinaname,e.sex,e.employmentStr,e.email,e.phone,e.becomeStaffDate,e.job,e.province,e.leaveDate, e.birthday,e.securityDate,e.school,e.profession,e.graduation, e.degree,e.accountAddr,e.accountPro,e.address,e.age,e.workAge,e.probationLimit,e.probationEnd,e.marriage,e.agreementEndDate,e.agreementLimit,e.positionEng, e.agreementStartDate,e.agreementTimes,e.area,e.workOld,e.material,e.bankCard,e.bear,e.idcard,e.nation,e.origin,e.train,e.securityCard,e.politicalFace,e.certificate,e.contact,e.contactPhone,e.fund,e.fundDate,o.name orgName,oc.name orgChildName,p.name positionName from simple_user u ";
				sql += "left join simple_person e ON(u.personId=e.id)  ";
				sql += "left join simple_org o ON(e.orgId=o.id)  ";
				sql += "left join simple_org oc ON(e.orgChildId=oc.id)  ";
				sql += "left join simple_position p ON(e.positionId=p.id)  ";
				sql+="where e.becomeStaffDate > NOW() ";
				if(roleId.contains(3)){
					//sql+=" and oc.pid='"+curForm.getOrgId()+"'";
					sql+="";
				}else if(roleId.contains(2)){
					sql+=" and e.orgChargeApprove=0 and e.orgChildId='"+curForm.getOrgChildId()+"'";
				}else if(roleId.contains(4)){
					//sql+=" and oc.pid='"+curForm.getOrgId()+"'";
					sql+=" and e.orgHeadApprove=0 and e.orgId='"+curForm.getOrgChildId()+"' and e.orgChargeApprove=1";
				}else if(roleId.contains(0)||roleId.contains(5)){
					//sql+=" and oc.pid='"+curForm.getOrgId()+"'";
					sql+="";
				}else{
					sql="";
				}
				sql = addWhere(sql, form, alias);
				Pager<UserForm> pager=this.personDao.findSQL(sql,alias,UserForm.class,false);
				if(pager!=null&& !pager.getDataRows().isEmpty()){
					for(UserForm pf:pager.getDataRows()){
						//获取员工部门主管及部门负责人姓名
						String org="select e.* from simple_user_roles ur left join simple_role r on(r.id=ur.roleId) ";
							   org += "left join simple_user u on(u.id=ur.userId) ";
							   org += "left join simple_person e on(e.id=u.personId) ";
							   org += "left join simple_org o on(o.id=e.orgChildId) ";
							   if(!StringUtil.isEmpty(pf.getOrgChildId())){
								   String orgCharge=org+"where  r.sn='CHARGE' and r.defaultRole=2  and o.id='"+pf.getOrgChildId()+"'";
								   List<UserForm> chargeForm=this.userDao.listSQL(orgCharge,UserForm.class,false);
								   if(chargeForm!=null&&chargeForm.size()>=0){
										  String chargeStr="";
										  for(UserForm f: chargeForm){
											  chargeStr+=f.getName()+" ";
										  }
										  pf.setOrgChargeName(chargeStr);
									  }
							   }
							  if(!StringUtil.isEmpty(pf.getOrgId())){
								  String orgHead=org+"where  r.sn='superior' and r.defaultRole=4  and o.id='"+pf.getOrgId()+"'";
								  
								  UserForm headForm=(UserForm) this.userDao.queryObjectSQL(orgHead,UserForm.class,false);
								  
								  if(headForm!=null){
									  pf.setOrgHeadName(headForm.getName());
								  }
							  }
						   forms.add(pf);
					}
				}
				DataGrid pd=new DataGrid();
				pd.setTotal(pager.getTotal());
				pd.setRows(forms);
				return pd;
				}
			}
			catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("加载列表信息异常");
		}
		return null;
	}

	private String addWhere(String sql, UserForm form, Map<String, Object> alias) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if(key.equals("englishname")){
					form.setName(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if(key.equals("chinaname")){
					form.setChinaname(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if(key.equals("becomeStaffDate")){
					form.setBecomeStaffDate(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
			}
		}
		if (StringUtil.isNotEmpty(form.getChinaname())) {
			try {
				sql += " and e.chinaname like :chinaname";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getName())) {
			try {
				sql += " and e.name like :name";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getPhone())) {
			try {
				sql += " and e.becomeStaffDate like :becomeStaffDate";
			} catch (Exception e) {
			}
		}
		return sql;
	}

	@Override
	public UserForm get(String id) {
		// TODO Auto-generated method stub
		try {
			String sql = "select u.*,e.hrNotice,e.orgChargeApprove,e.orgHeadApprove,e.approveStatus,e.orgChargeResult,e.orgHeadResult,e.orgChargeName,e.orgHeadName,e.orgChargeRemark,e.orgHeadRemark,e.chinaname,e.email,e.sex,e.employmentStr,e.phone,e.job,e.province, e.orgId, e.positionId, e.becomeStaffDate,e.birthday,e.securityDate,e.school,e.profession,e.graduation, e.degree,e.accountAddr,e.accountPro,e.address,e.age,e.workAge,e.probationLimit,e.probationEnd,e.marriage,e.agreementEndDate,e.agreementLimit,e.positionEng,e.orgChildId,e.agreementStartDate,e.agreementTimes,e.area,e.workOld,e.material,e.bankCard,e.bear,e.idcard,e.nation,e.origin,e.train,e.securityCard,e.politicalFace,e.certificate,e.contact,e.contactPhone,e.fund,e.fundDate from simple_user u ";
			sql += " left join simple_person e ON(u.personId=e.id) ";
			sql += " where u.id=?";
			UserForm form = (UserForm) this.userDao.queryObjectSQL(sql, new Object[] { id }, UserForm.class, false);
			
			/*判断审批状态*/
			if(StringUtil.isEmpty(form.getOrgChargeResult())){
				form.setOrgChargeResult("未审批");
			}
			if(StringUtil.isEmpty(form.getOrgHeadResult())){
				form.setOrgHeadResult("未审批");
			}
			
			/*获取部门主管与部门负责人英文名*/
			/*if(StringUtil.isEmpty(form.getOrgChargeName())){
				String sqlorg="select u.*,e.email,o.pid orgId from simple_role r  LEFT JOIN simple_user_roles ro ON (r.id=ro.roleId) ";
				sqlorg+="LEFT JOIN simple_user u ON (ro.userId=u.id) LEFT JOIN simple_person e ON (u.personId=e.id) ";
				sqlorg+="LEFT JOIN  simple_org  o ON (e.orgChildId=o.id)  ";
				sqlorg+="where  r.sn='CHARGE' and r.defaultRole=2  and o.id='"+form.getOrgChildId()+"'";
		         UserForm OrgUserChild=(UserForm) this.personDao.queryObjectSQL(sqlorg, UserForm.class, false); 
		         if(OrgUserChild!=null){
			         UserForm OrgUser=null;
			         if(StringUtil.isEmpty(form.getOrgHeadName())){
			        	 if(StringUtil.isEmpty(OrgUserChild.getOrgId())){
			        		 form.setOrgHeadName(OrgUserChild.getName());
			        	 }
			        	 else{
			        		 String sqll="select u.*,e.email,o.pid orgId from simple_role r  LEFT JOIN simple_user_roles ro ON (r.id=ro.roleId) ";
						      sqll+="LEFT JOIN simple_user u ON (ro.userId=u.id) LEFT JOIN simple_person e ON (u.personId=e.id) ";
					          sqll+="LEFT JOIN  simple_org  o ON (e.orgChildId=o.id)  ";
					          sqll+="where  r.sn='superior' and r.defaultRole=4  and o.id='"+OrgUserChild.getOrgId()+"'";
					          OrgUser=(UserForm) this.personDao.queryObjectSQL(sqll, UserForm.class, false); 
					         if(OrgUser!=null){
					        	 form.setOrgHeadName(OrgUser.getName());
					         }else{
					        	 form.setOrgHeadName("");
					         }
			        	 }
				         form.setOrgChargeName(OrgUserChild.getName());
			         }
		         }else{
		        	 form.setOrgChargeName("");
		         }
			}*/
			//获取员工部门主管及部门负责人姓名
			String org="select e.* from simple_user_roles ur left join simple_role r on(r.id=ur.roleId) ";
				   org += "left join simple_user u on(u.id=ur.userId) ";
				   org += "left join simple_person e on(e.id=u.personId) ";
				   org += "left join simple_org o on(o.id=e.orgChildId) ";
				   if(!StringUtil.isEmpty(form.getOrgChildId())){
					   String orgCharge=org+"where  r.sn='CHARGE' and r.defaultRole=2  and o.id='"+form.getOrgChildId()+"'";
					   List<UserForm> chargeForm=this.userDao.listSQL(orgCharge,UserForm.class,false);
					   if(chargeForm!=null&&chargeForm.size()>=0){
							  String chargeStr="";
							  for(UserForm f: chargeForm){
								  chargeStr+=f.getName()+" ";
							  }
							  form.setOrgChargeName(chargeStr);
						  }
				   }
				  if(!StringUtil.isEmpty(form.getOrgId())){
					  String orgHead=org+"where  r.sn='superior' and r.defaultRole=4  and o.id='"+form.getOrgId()+"'";
					  
					  UserForm headForm=(UserForm) this.userDao.queryObjectSQL(orgHead,UserForm.class,false);
					  
					  if(headForm!=null){
						  form.setOrgHeadName(headForm.getName());
					  }
				  }
			return form;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("加载用户信息异常：", e);
		}
	}

	
	@Override
	public Msg approvaltrue(UserForm form) {
		try{
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
			UserEntity u=this.userDao.load(UserEntity.class, form.getId());
			PersonEntity p=this.personDao.load(PersonEntity.class, u.getPersonId());
			//RoleEntity r=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
			if(roleName.contains("superior") && roleId.contains(4)){
				if(p.getOrgHeadApprove()==1){
					return new Msg(true,"审批已通过，不可重复");
				}
				p.setOrgHeadApprove(1);
				p.setOrgHeadResult("审批通过");
			    p.setOrgHeadName(WebContextUtil.getCurrentUser().getUser().getName());
			    p.setOrgHeadRemark(form.getOrgHeadRemark());
			    p.setApproveStatus(1);
			    this.personDao.update(p);
			    return new Msg(true,"审批通过");
			}
			if(roleName.contains("CHARGE") && roleId.contains(2)){
				if(p.getOrgChargeApprove()==1){
					return new Msg(true,"审批已经通过，不可重复");
				}
				p.setOrgChargeApprove(1);
				p.setOrgChargeResult("审批通过");
				p.setOrgChargeName(WebContextUtil.getCurrentUser().getUser().getName());
				p.setOrgChargeRemark(form.getOrgChargeRemark());
			    p.setApproveStatus(0);
				this.personDao.update(p);
				return new Msg(true,"审批通过");
			}
			else{
		    	return new Msg(true,"没有审批权限");
		    }
			}catch(Exception e){
				e.printStackTrace();
				return new Msg(false,"审批失败");
			}
	}
	
	@Override
	public Msg approvalfalse(UserForm form) {
		try{
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
			UserEntity u=this.userDao.load(UserEntity.class, form.getId());
			PersonEntity p=this.personDao.load(PersonEntity.class, u.getPersonId());
			//RoleEntity r=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
			if(roleName.contains("superior") && roleId.contains(4)){
				if(p.getOrgHeadApprove()==2){
					return new Msg(true,"已经审批不通过，不可重复");
				}
				if(p.getOrgHeadApprove()!=2){
			        p.setOrgHeadApprove(2);
			        p.setOrgHeadResult("审批不通过");
				}
			    p.setOrgHeadName(WebContextUtil.getCurrentUser().getUser().getName());
				p.setOrgHeadRemark(form.getOrgHeadRemark());
				p.setApproveStatus(2);
			    this.personDao.update(p);
			    return new Msg(true,"总监审批不通过");
			}
			if(roleName.contains("CHARGE") && roleId.contains(2)){
				if(p.getOrgChargeApprove()==2){
					return new Msg(true,"已经审批不通过，不可重复");
				}
				if(p.getOrgChargeApprove()!=2){
				  p.setOrgChargeApprove(2);
				  p.setOrgChargeResult("审批不通过");
				}
				p.setOrgChargeName(WebContextUtil.getCurrentUser().getUser().getName());
				p.setOrgChargeRemark(form.getOrgChargeRemark());
			    p.setApproveStatus(2);
				this.personDao.update(p);
				return new Msg(true,"审批不通过");
			}
			else{
		    	return new Msg(false,"没有审批权限");
		    }
			}catch(Exception e){
				e.printStackTrace();
				return new Msg(false,"审批失败");
			}
	}

	
	@Override
	public Msg approvalnotice(String id) {
		List<Integer> roleId=new ArrayList<Integer>();
		List<String> roleName=new ArrayList<String>();
		//判断当前用户是否具有多个角色
		if(WebContextUtil.getCurrentUser().getUser().getRole_ids().contains(",")){
			String[] roleIds=WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
			for(String ids:roleIds){
				RoleForm role=(RoleForm) this.roleDao.queryObjectSQL("select * from simple_role where id=?",new Object[]{ids},RoleForm.class,false);
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
		List<String> emailList=new ArrayList<String>();
		//RoleEntity  r=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
		UserEntity ur=this.userDao.load(UserEntity.class, id);
		PersonEntity p=this.personDao.load(PersonEntity.class, ur.getPersonId());
		String usersql="select u.account,e.* from simple_user u left join simple_person e on(u.personId=e.id) where u.id=?";
		UserForm u=(UserForm) this.userDao.queryObjectSQL(usersql,new Object[]{id},UserForm.class,false);
		if(u!=null){
			String sql="select u.*,e.email,o.pid orgId from simple_role r left join simple_user_roles ro ON (r.id=ro.roleId) ";
			      sql+="left join simple_user u ON (ro.userId=u.id) left join simple_person e ON (u.personId=e.id) ";
		          sql+="left join  simple_org  o ON (e.orgChildId=o.id)  ";
		          sql+="where  r.sn='CHARGE' and r.defaultRole=2  and o.id='"+u.getOrgChildId()+"'";
		          List<UserForm> orgUserChild=this.userDao.listSQL(sql, UserForm.class, false);
		         if(orgUserChild!=null&&orgUserChild.size()>0){
			         for(UserForm form:orgUserChild){ 
			        	 emailList.add(form.getEmail()); 
			         }
			         String sqll="select u.*,e.email,o.pid orgId from simple_role r  LEFT JOIN simple_user_roles ro ON (r.id=ro.roleId) ";
				      sqll+="LEFT JOIN simple_user u ON (ro.userId=u.id) LEFT JOIN simple_person e ON (u.personId=e.id) ";
			          sqll+="LEFT JOIN  simple_org  o ON (e.orgChildId=o.id)  ";
			          sqll+="where  r.sn='superior' and r.defaultRole=4  and o.id='"+u.getOrgId()+"'";
			          UserForm OrgUser=(UserForm) this.personDao.queryObjectSQL(sqll, UserForm.class, false); 
			         if(OrgUser!=null){
			        	 emailList.add(OrgUser.getEmail());
			         }
			         if(roleName.contains("HR") && roleId.contains(3)){
			        	 // String[] str=new String[]{"gerrylehuang@can-dao.com","1980921973@qq.com"};
			        	  String[] emailArray=emailList.toArray(new String[0]);
			        	   if(emailArray!=null){
						     //SendEmailUtil.sendMail("审核通知", u.getAccount()+"  "+ p.getName()+"  "+p.getChinaname()+"的转正日期："+p.getBecomeStaffDate()+"\n请审批",emailArray);
			        	   }
						   p.setHrNotice(1);
						   this.personDao.update(p);
						  return new Msg(true,"通知成功");
						}else{
							return new Msg(false,"无权限发送邮件");
						}
		         }  
		         else{
		        	 return new Msg(false,"发送邮件失败");
		         }
				
		}else{
			return new Msg(false,"发送邮件失败");
		}
		
	}
	
	
	/**
	 * 获取当前用户角色编号
	 */
	public int getCurrentUserDefaultRole(){
		if(WebContextUtil.getCurrentUser().getUser().getApproveAuth().equals("1")){
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
		}else{
			return 1;
		}
	}
	
}
