package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.tools.ant.taskdefs.SendEmail;
import org.codehaus.jackson.map.util.BeanUtil;
import org.hibernate.service.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
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
import com.rosense.module.system.entity.TransferEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.TransferEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.ITransferService;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.TransferForm;
import com.rosense.module.system.web.form.UserForm;

import net.sf.json.JSONObject;

@Service("transfeService")
@Transactional
public class TransferService implements ITransferService {

	
	@Inject
	private IBaseDao<TransferEntity> transferDao;
	@Inject
	private IBaseDao<RoleEntity> roleDao;
	@Inject
	private IBaseDao<OrgEntity> orgDao;
	@Inject
	private IBaseDao<UserEntity> userDao;
	@Inject
	private IBaseDao<PersonEntity> personDao;
	/**
	 * 添加申请信息
	 */
	@Override
	public Msg add(TransferForm form) {
		// TODO Auto-generated method stub
		try{
			List<String> list=new ArrayList<String>();
			String sqluser="select * from simple_user where status=0";
			List<UserForm> userList=this.userDao.listSQL(sqluser, UserForm.class,false);
			if(userList!=null){
				for(UserForm user:userList){
					list.add(user.getAccount());
				}
			}
			if(list.contains(form.getAccount())){
			form.setNewOrgChargeId(WebContextUtil.getCurrentUser().getUser().getId());
			form.setNewOrgChargeName(WebContextUtil.getCurrentUser().getUser().getName());
			String sqlHead="select e.* from simple_role r left join simple_user_roles ur on(r.id=ur.roleId) left join  simple_user u on(ur.userId=u.id)  left join simple_person e on(u.personId=e.id) ";
			 sqlHead+="left join simple_org o on(o.id=e.orgId) ";
			 sqlHead+="where r.sn='superior' and r.defaultRole=4 and o.id='"+form.getNewOrgId()+"'";
			 UserForm headUser=(UserForm) this.userDao.queryObjectSQL(sqlHead, UserForm.class,false);
			 if(headUser!=null){
				 form.setNewOrgHeadName(headUser.getName());
			 }
			 form.setTransferMayName("May luo");
			 form.setTransferRavicName("Ravic li");
			final TransferEntity t=new TransferEntity();
			BeanUtils.copyNotNullProperties(form, t);
			this.transferDao.add(t);
			if(!StringUtil.isEmpty(headUser.getEmail())){
				String[] str=new String[]{headUser.getEmail(),"gerrylehuang@can-dao.com","1980921973@qq.com"};
				//发送邮件通知部门负责人审批
				//SendEmailUtil.sendMail("申请通知\n", form.getAccount()+"  "+ form.getName()+"  "+form.getChinaname()+"转调："+"\n请审批",str);
				return new Msg(true,"添加成功");
			}else{
				return new Msg(false,"发送邮件失败，请重新操作！");
			}
			
			}
			else{
				return new Msg(false,"该帐号不存在,无法添加");
			}
		   }catch (Throwable e) {
				e.printStackTrace();
				return new Msg(false, "添加失败！");
			}
		   

	}
	
	/**
	 * 查询申请信息
	 * @param form
	 * @return
	 */
	@Override
	public DataGrid select(TransferForm form) {
		String name=WebContextUtil.getCurrentUser().getUser().getName();
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
		try{
			if (null == form.getTransApplyDate()) {
				SystemContext.setSort("t.transApplyDate");
				SystemContext.setOrder("desc");
			}
			List<TransferForm> forms=new ArrayList<TransferForm>();
			Map<String, Object> alias=new HashMap<String,Object>();
			//RoleEntity role=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
			String sql="";
			
			if(roleId.contains(0) && roleName.contains("ADMIN")){
				sql="select t.* from simple_transfer t where 1=1";
			}else if(roleId.contains(2) && roleName.contains("CHARGE")){
				//部门主管获取该子部门转调申请信息
				sql="select t.* from simple_transfer t where  t.newOrgChargeName like '"+name.substring(0,name.indexOf(" "))+"%'";
			}else if(WebContextUtil.getCurrentUser().getUser().getName().toLowerCase().equals("may luo")){
				//超级管理员以及具有权限的HR获取所有转调申请信息
				sql="select t.* from simple_transfer t  where  t.orgMayApprove=0 and  t.orgHeadApprove=1 ";
			}else if(roleId.contains(4) && roleName.contains("superior")){
				//部门负责人获取该部门的所有转调申请信息
				sql="select t.* from simple_transfer t where t.orgHeadApprove=0 and t.newOrgHeadName like '"+name.substring(0,name.indexOf(" "))+"%'";
			}else if(WebContextUtil.getCurrentUser().getUser().getName().toLowerCase().equals("ravic li")){
				//超级管理员以及具有权限的HR获取所有转调申请信息
				sql="select t.* from simple_transfer t  where t.orgRavicApprove=0 and t.orgMayApprove=1 ";
			}
			
			sql=addWhere(sql,form,alias);
			if(!sql.equals("")){
				Pager<TransferForm> pager=this.transferDao.findSQL(sql, alias,TransferForm.class,false);
				if(pager!=null&& !pager.getDataRows().isEmpty()){
					for(TransferForm pf:pager.getDataRows()){
						String sqlOrg="select o.name orgName,oc.name orgChildName,newo.name newOrgName,newoc.name newOrgChildName from simple_org o,simple_org oc,simple_org newo, simple_org newoc ";
						sqlOrg+="where o.id='"+pf.getOrgId()+"' and oc.id='"+pf.getOrgChildId()+"' and newo.id='"+pf.getNewOrgId()+"' and newoc.id='"+pf.getNewOrgChildId()+"'";
						String sqlPisitionId="select p.name positionName,np.name newPositionName from simple_position p,simple_position np where p.id='"+pf.getPositionId()+"' and np.id='"+pf.getNewPositionId()+"'";
						TransferForm orgForm=(TransferForm) this.transferDao.queryObjectSQL(sqlOrg,TransferForm.class,false);
						TransferForm positionForm=(TransferForm) this.transferDao.queryObjectSQL(sqlPisitionId,TransferForm.class,false);
						pf.setOrgName(orgForm.getOrgName());
						pf.setOrgChildName(orgForm.getOrgChildName());
						pf.setNewOrgName(orgForm.getNewOrgName());
						pf.setNewOrgChildName(orgForm.getNewOrgChildName());
						pf.setPositionName(positionForm.getPositionName());
						pf.setNewPositionName(positionForm.getNewPositionName());
						forms.add(pf);
					}
				}
				DataGrid pd=new DataGrid();
				pd.setTotal(pager.getTotal());
				pd.setRows(forms);
				return pd;
			}else{
				return null;
			}
			}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("加载列表信息异常");
		}
	}

	private String addWhere(String sql, TransferForm form, Map<String, Object> alias) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if(key.equals("englishname")){
					form.setName(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if(key.equals("chinaname")){
					form.setChinaname(StringUtil.getEncodePra(jsonObject.get(key).toString()));
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
		return sql;
	}

	
	/**
	 * 获取申请信息
	 */
	@Override
	public TransferForm get(String id) {
		// TODO Auto-generated method stub
		try {
			String sql="select t.*,o.name orgName,oc.name orgChildName,newo.name newOrgName,newoc.name newOrgChildName,p.name positionName,newp.name newPositionName from simple_transfer t ";
			sql+="left join simple_org o on(t.orgId=o.id) ";
			sql+="left join simple_org oc on(t.orgId=oc.id) ";
			sql+="left join simple_org newo on(t.orgId=newo.id) ";
			sql+="left join simple_org newoc on(t.orgId=newoc.id) ";
			sql+="left join simple_position p on(t.positionId=p.id) ";
			sql+="left join simple_position newp on(t.positionId=newp.id) ";
			sql+="where t.id=? ";
			TransferForm form = (TransferForm) this.transferDao.queryObjectSQL(sql, new Object[] { id }, TransferForm.class, false);
			return form;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("加载用户信息异常：", e);
		}
	}

	
	@Override
	public Msg approvaltrue(TransferForm form) {
		try{
			
			TransferEntity t=this.transferDao.load(TransferEntity.class, form.getId());
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
			//RoleEntity r=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
			if(t.getTransApplyStatus()==2){
				return new Msg(false,"申请已经撤销，不可审批");
			}
			else{
				if(WebContextUtil.getCurrentUser().getUser().getName().toLowerCase().equals("may luo")){
					
					if(t.getOrgMayApprove()==1){
						return new Msg(true,"审批已经通过，不可重复");
					}
				    t.setOrgMayApprove(1);
				    t.setTransApplyStatus(0);
				    t.setTransferMayResult("审批通过");
					t.setTransferMayName(WebContextUtil.getCurrentUser().getUser().getName());
					t.setTransferMayRemark(form.getTransferMayRemark());
					this.transferDao.update(t);
					return new Msg(true,"May审批通过");
				}else if(roleId.contains(4) && roleName.contains("superior")){
					if(t.getOrgHeadApprove()==1){
						return new Msg(true,"审批已经通过，不可重复");
					}
					t.setOrgHeadApprove(1);
					t.setTransApplyStatus(0);
					t.setNewOrgHeadResult("审批通过");
				    t.setNewOrgHeadName(WebContextUtil.getCurrentUser().getUser().getName());
				    t.setNewOrgHeadRemark(form.getNewOrgHeadRemark());
				    this.transferDao.update(t);
				    return new Msg(true,"部门负责人审批通过");
				}
				
				if(WebContextUtil.getCurrentUser().getUser().getName().toLowerCase().equals("ravic li")){
					
					if(t.getOrgRavicApprove()==1){
						return new Msg(true,"审批已经通过，不可重复");
					}
				    t.setOrgRavicApprove(1);
				    t.setTransApplyStatus(1);
				    t.setTransferRavicResult("审批通过");
					t.setTransferRavicName(WebContextUtil.getCurrentUser().getUser().getName());
					t.setTransferRavicRemark(form.getTransferRavicRemark());
					this.transferDao.update(t);
					return new Msg(true,"Ravic审批通过,通知Shadow、May执行");
				}
				else{
			    	return new Msg(true,"没有审批权限");
			    }
			}
			}catch(Exception e){
				e.printStackTrace();
				return new Msg(false,"审批失败");
			}
	}
	
	@Override
	public Msg approvalfalse(TransferForm form) {
		try{
				TransferEntity p=this.transferDao.load(TransferEntity.class, form.getId());
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
				//RoleEntity r=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
				if(p.getTransApplyStatus()==2){
					return new Msg(false,"申请已经撤销，不可审批");
				}
				else{
                     if(WebContextUtil.getCurrentUser().getUser().getName().toLowerCase().equals("may luo")){
						
						if(p.getOrgMayApprove()==2){
							return new Msg(true,"审批不通过，不可重复");
						}
						if(p.getOrgMayApprove()!=2){
					        p.setOrgMayApprove(2);
					        p.setTransferMayResult("审批不通过");
						}
					    p.setTransferMayName(WebContextUtil.getCurrentUser().getUser().getName());
						p.setTransferMayRemark(form.getTransferMayRemark());
						p.setTransApplyStatus(3);
					    this.transferDao.update(p);
					    return new Msg(true,"总监审批不通过");
					}else if(roleId.contains(4) && roleName.contains("superior")){
						if(p.getOrgHeadApprove()==2){
							return new Msg(true,"审批不通过，不可重复");
						}
					     p.setOrgHeadApprove(2);
					     p.setNewOrgHeadResult("审批不通过");
					     p.setNewOrgHeadName(WebContextUtil.getCurrentUser().getUser().getName());
						 p.setNewOrgHeadRemark(form.getNewOrgHeadRemark());
						 p.setTransApplyStatus(3);
					    this.transferDao.update(p);
					    return new Msg(true,"总监审批不通过");
					}
					
					if(WebContextUtil.getCurrentUser().getUser().getName().toLowerCase().equals("ravic li")){
						
						if(p.getOrgRavicApprove()==2){
							return new Msg(true,"已审批不通过，不可重复");
						}
						if(p.getOrgRavicApprove()!=2){
					        p.setOrgRavicApprove(2);
					        p.setTransferRavicResult("审批不通过");
						}
						p.setTransApplyStatus(3);
					    p.setTransferRavicName(WebContextUtil.getCurrentUser().getUser().getName());
						p.setTransferRavicRemark(form.getTransferRavicRemark());
					    this.transferDao.update(p);
					    return new Msg(true,"不通过");
					}
					else{
				    	return new Msg(false,"没有审批权限");
				    }
				}
			}catch(Exception e){
				e.printStackTrace();
				return new Msg(false,"审批失败");
			}
	}

	
	@Override
	public Msg approvalnotice(String id) {
		try{
			//RoleEntity  role=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
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
			if(roleId.contains(3)){
				TransferEntity p=this.transferDao.load(TransferEntity.class, id);
				if(p.getOrgHeadApprove()==1&&p.getOrgMayApprove()==1&&p.getOrgRavicApprove()==1){
					String sqlorg="select * from simple_org where id='"+p.getNewOrgChildId()+"'";
					OrgForm o=(OrgForm) this.orgDao.queryObjectSQL(sqlorg,OrgForm.class,false);
				    if(o!=null){
				    	//SendEmailUtil.sendMail("转调通知",p.getName()+' '+p.getAccount()+' '+p.getChinaname()+p.getTransApplyDate()+"转到"+o.getName(), this.userEmails().toArray(new String[0]));
					    return new Msg(true,"邮件发送成功！");
				    }else{
				    	return new Msg(false,"邮件发送失败！");
				    }
				}else{
					return new Msg(false,"审批不通过，无法发送邮件！");
				}
				
			}else{
				return new Msg(false,"无权发送邮件！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"邮件发送失败！");
		}
	}

	/*
	 * 更新信息
	 * 
	 */
	@Override
	public Msg update(TransferForm form) {
		// TODO Auto-generated method stub
		try{
		    //RoleEntity role=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
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
		    if(roleName.contains("CHARGE") && roleId.contains(2)&& WebContextUtil.getCurrentUser().getUser().getName().equals(form.getNewOrgChargeName())){
		    	TransferEntity tEntity=this.transferDao.load(TransferEntity.class,form.getId());
				String sqlHead="select u.* from simple_role r left join simple_user_roles ur on(r.id=ur.roleId) left join  simple_user u on(ur.userId=u.id)  left join simple_person e on(u.personId=e.id) ";
				 sqlHead+="left join simple_org o on(o.id=e.orgChildId) ";
				 sqlHead+="where r.sn='superior' and r.defaultRole=4 and o.id='"+form.getNewOrgId()+"'";
				 UserForm headUser=(UserForm) this.userDao.queryObjectSQL(sqlHead, UserForm.class,false);
				 if(headUser!=null){
					 form.setNewOrgHeadName(headUser.getName());
				 }
				BeanUtils.copyNotNullProperties(form, tEntity);
				this.transferDao.update(tEntity);
				return new Msg(true,"修改成功！");
		    }else{
		    return new Msg(false,"无权修改信息！");
		    }
			}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"修改失败！");
		}
	}
	
   /**
    * 注销申请
    * 
    */
	@Override
	public Msg delete(TransferForm form) {
		// TODO Auto-generated method stub
		try{
			TransferEntity t=this.transferDao.load(TransferEntity.class,form.getId()); 
			if(t.getNewOrgChargeName().equals(WebContextUtil.getCurrentUser().getUser().getName())){
			if(t.getTransApplyStatus()==2){
				return new Msg(false,"已经撤销，不可重复撤销");
			}else{
				t.setTransApplyStatus(2);
				this.transferDao.update(t);
				return new Msg(true,"撤销成功");
			}
		}
		else{
			return new Msg(false,"无权撤销");
		}
	}catch(Exception e){
		e.printStackTrace();
		return new Msg(false,"撤销失败");
	}
	}
	
	
	/**
	 * 获取所有用户邮箱
	 * @return
	 */
	private List<String> userEmails(){
	    try{
	    	List<String> emailList=new ArrayList<String>();
	    	String sqlEmail="select p.email from simple_person p where 1=1";
	    	List<UserForm> userList=this.personDao.listSQL(sqlEmail, UserForm.class,false);
	    	if(userList!=null&&userList.size()>0){
		    	for(UserForm form:userList){
		    		if(!StringUtil.isEmpty(form.getEmail())){
		    			emailList.add(form.getEmail());
		    		}
		    	}
		    	return emailList;
	    	}else{
	    		return null;
	    	}
	    }catch(Exception e){
	    	e.printStackTrace();
	    	return null;
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
				 }else if(roleId.contains(5)){
					 //经理
					 return 5;
				 }else if(roleId.contains(4)&&roleId.contains(3)){
					 //部门总监兼HR角色
					 return 43;
				 }else if(roleId.contains(4)){
					 //部门总监
					 return 4;
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
