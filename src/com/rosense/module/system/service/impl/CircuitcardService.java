package com.rosense.module.system.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.management.relation.Role;

import org.springframework.stereotype.Service;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.SendEmailUtil;
import com.rosense.basic.util.StringUtil;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.CircuicardEntity;
import com.rosense.module.system.entity.OrgEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.CircuicardEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.ICircuitcardService;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

@Service("circuitcardService")
@Transactional
public class CircuitcardService implements ICircuitcardService {

	
	@Inject
	private IBaseDao<CircuicardEntity> circuicardDao;
	@Inject
	private IBaseDao<RoleEntity> roleDao;
	@Inject
	private IBaseDao<OrgEntity> orgDao;
	@Inject
	private IBaseDao<UserEntity> userDao;
	@Inject
	private IBaseDao<CircuicardEntity> cardDao;
	
	/**
	 * 添加申请信息
	 */
	@Override
	public Msg add(UserForm form) {
		// TODO Auto-generated method stub
		try{
			//获取申请时间
			List<String> emails=new ArrayList<String>();
			SimpleDateFormat pf=new SimpleDateFormat("yyyy/MM/dd");
		    form.setApplyDate(pf.format(new Date())); ;
			CircuicardEntity card=new CircuicardEntity();
			BeanUtils.copyNotNullProperties(form, card);
			//获取部门主管的邮箱
			String susql="select * from simple_person where name='"+form.getOrgChargeName()+"'";
			UserForm suForm=(UserForm) this.userDao.queryObjectSQL(susql, UserForm.class,false);
			if(suForm!=null){
				emails.add(suForm.getEmail());
			}
			
			//获取HR的邮箱
			String sql="select e.* from simple_role r left join simple_user_roles ur on(r.id=ur.roleId) "
					+ "left join simple_user u on(ur.userId=u.id) "
					+ "left join simple_person e on(u.personId=e.id) where r.defaultRole=3";
			List<UserForm> HRform =this.userDao.listSQL(sql, UserForm.class,false);
			if(HRform!=null&& HRform.size()>0){
				for(UserForm user:HRform){
					emails.add(user.getEmail());
				}
				
			}
			//SendEmailUtil.sendMail("印制名片申请", card.getName()+" "+card.getChinaname()+" "+suForm.getOrgName()+"部门", emails.toArray(new String[0]));
			this.cardDao.add(card);
			return new Msg(true,"申请成功");
			
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"申请失败");
		}
	}
	
	/**
	 * 查询印制名片细信息
	 */
	@Override
	public DataGrid select(UserForm form) {
		// TODO Auto-generated method stub
		List<UserForm> forms=new ArrayList<UserForm>();
		boolean flag=false;
		if(WebContextUtil.getCurrentUser().getUser().getRole_ids()!=null){
			String[] roleIds=WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
			for(String roleId:roleIds){
				 RoleEntity role=this.roleDao.load(RoleEntity.class,roleId);
			     if(role.getDefaultRole()==3){
			    	 flag=true;
			     }
			}
			
		}
		DataGrid pd=new DataGrid();
		String sql="select c.*,o.name orgName,p.name positionName from simple_circuitcard c left join simple_org o on(o.id=c.orgId) "
				+ "left join simple_position p on(p.id=c.positionId) "
				+ "where 1=1";
		Pager<UserForm> pager=this.cardDao.findSQL(sql, UserForm.class, false);
		if(!pager.getDataRows().isEmpty()&&pager!=null){
			for(UserForm pf:pager.getDataRows()){
				//HR获取所有信息
				if(flag||WebContextUtil.getCurrentUser().getUser().getName().equals("admin")){
					forms.add(pf);
				}
				else{
					//部门主管获取该部门员工申请信息
					if(pf.getOrgChargeName().equals(WebContextUtil.getCurrentUser().getUser().getName())|| pf.getName().equals(WebContextUtil.getCurrentUser().getUser().getName())){
						forms.add(pf);
					}
				}
			}
		pd.setTotal(pager.getTotal());
		pd.setRows(forms);
		}
		return pd;
	}
	
	/**
	 * 获取印制名片信息
	 * @param id
	 * @return
	 */
	@Override
	public UserForm get(String id) {
		// TODO Auto-generated method stub
		try{
		String sql="select c.*,o.name orgName,p.name positionName from simple_circuitcard c left join simple_org o on(o.id=c.orgId) "
				+ "left join simple_position p on(p.id=c.positionId) "
				+ "where c.id=?";
		UserForm userForm=(UserForm) this.cardDao.queryObjectSQL(sql,new Object[]{id},UserForm.class,false);
		if(userForm!=null){
			return userForm;
		}else{
			return null;
		}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 修改申请信息
	 * @param form
	 * @return
	 */
	@Override
	public Msg update(UserForm form) {
		// TODO Auto-generated method stub
		try{
		CircuicardEntity card=this.cardDao.load(CircuicardEntity.class, form.getId());
		if(card.getApplyStatus()!=0){
			return new Msg(false,"已审批，不可编辑");
		}
		String sql="";
		if(WebContextUtil.getCurrentUser().getUser().getRole_ids()!=null){
			String strRoles[] =WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
			for(String roleId:strRoles){
				RoleEntity role=this.roleDao.load(RoleEntity.class,roleId);
				 sql="select e.* from simple_role r left join simple_user_roles ur on(r.id=ur.roleId) "
						+ "left join simple_user u on(ur.userId=u.id) "
						+ "left join simple_person e on(u.personId=e.id) ";
						
				if(role.getDefaultRole()==0||role.getDefaultRole()==3){
					//普通员工获取上级信息
					sql+= "left join simple_org o on(e.orgChildId=o.id) ";
					break;
				}else{
					//主管获取上级信息
					sql+= "left join simple_org o on(e.orgId=o.id) ";
				}
			}
				String sqlorg=sql+ "where o.id='"+card.getOrgId()+"' and u.name='"+form.getOrgChargeName()+"'";
				String sqHR=sql+"where r.defaultRole=3";
				//获取部门主管的邮箱
				UserForm orgForm=(UserForm) this.userDao.queryObjectSQL(sqlorg,UserForm.class,false);
				String emails="";
				if(orgForm!=null){
					emails+=orgForm.getEmail()+",";
				}
				//获取HR的邮箱
				List<UserForm> HRform =this.userDao.listSQL(sqHR, UserForm.class,false);
				if(HRform!=null&& HRform.size()>0){
					for(UserForm user:HRform){
						emails+=user.getEmail()+",";
					}
					
				}
				String[] arrayEmails=emails.substring(0,emails.length()-1).split(",");
				//SendEmailUtil.sendMail("印制名片申请", card.getName()+" "+card.getChinaname()+" "+card.getOrgName()+"部门", arrayEmails);
				BeanUtils.copyNotNullProperties(form, card);
				this.cardDao.update(card);
				return new Msg(true,"修改成功！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"修改失败！");
		}
		return new Msg(false,"修改失败！");
	}
	
	/**
	 * 审批通过
	 */
	@Override
	public Msg approvaltrue(UserForm form) {
		// TODO Auto-generated method stub
		CircuicardEntity c=this.circuicardDao.load(CircuicardEntity.class, form.getId());
		if(WebContextUtil.getCurrentUser().getUser().getRole_ids()!=null){
			String[] roleIds= WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
			for(String roleId:roleIds){
				RoleEntity r=this.roleDao.load(RoleEntity.class, roleId);
				if(r!=null){
				if((r.getSn().equals("CHARGE")&&r.getDefaultRole()==2&&c.getOrgChargeName().equals(WebContextUtil.getCurrentUser().getUser().getName()))
					|| (r.getSn().equals("superior")&&r.getDefaultRole()==4&&c.getOrgChargeName().equals(WebContextUtil.getCurrentUser().getUser().getName()))
					|| r.getDefaultRole()==5&&c.getOrgChargeName().equals(WebContextUtil.getCurrentUser().getUser().getName())){
					if(c.getOrgChargeApprove()==1){
						return new Msg(true,"已通过审批，不可重复！");
					}
					c.setOrgChargeApprove(1);
					c.setOrgChargeRemark(form.getApplyRemark());
					c.setOrgChargeResult("审批通过|！");
					c.setApplyStatus(1);
					return new Msg(true,"审批成功！");
				 }
				}
			}
		}
		return new Msg(false,"审批失败！");
	}

	
	/**
	 * 审批不通过
	 */
	@Override
	public Msg approvalfalse(UserForm form) {
		// TODO Auto-generated method stub
			CircuicardEntity c=this.circuicardDao.load(CircuicardEntity.class, form.getId());
			if(WebContextUtil.getCurrentUser().getUser().getRole_ids()!=null){
				String[] roleIds= WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
				for(String roleId:roleIds){
					RoleEntity r=this.roleDao.load(RoleEntity.class, roleId);
				if(r!=null){
					if((r.getSn().equals("CHARGE")&&r.getDefaultRole()==2&&c.getOrgChargeName().equals(WebContextUtil.getCurrentUser().getUser().getName()))
						|| (r.getSn().equals("superior")&&r.getDefaultRole()==4&&c.getOrgChargeName().equals(WebContextUtil.getCurrentUser().getUser().getName()))
						|| r.getDefaultRole()==5&&c.getOrgChargeName().equals(WebContextUtil.getCurrentUser().getUser().getName())){
						if(c.getOrgChargeApprove()==2){
							return new Msg(true,"已审批不通过，不可重复！");
						}
						c.setOrgChargeApprove(2);
						c.setOrgChargeRemark(form.getApplyRemark());
						c.setOrgChargeResult("审批不通过|！");
						c.setApplyStatus(3);
						return new Msg(true,"审批成功！");
				   }
				}
		     }
		}
		return new Msg(false,"审批失败！");
	}

	/**
	 * 邮件通知
	 */
	@Override
	public Msg approvalnotice(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 申请完成
	 */
	public Msg complete(UserForm form){
		CircuicardEntity card=this.cardDao.load(CircuicardEntity.class,form.getId());
		if(WebContextUtil.getCurrentUser().getUser().getRole_ids()!=null){
			String strOrg[] =WebContextUtil.getCurrentUser().getUser().getRole_ids().split(",");
			for(String strId: strOrg){
				RoleEntity role=this.roleDao.load(RoleEntity.class,strId);
				if(role!=null){
					if(role.getDefaultRole()==3){
						if(card!=null){
							card.setApplyStatus(2);
							this.cardDao.update(card);
							return new Msg(true,"完成");
						}else{
							return new Msg(false,"申请失败！");
						}
					}else{
						return new Msg(false,"无权操作");
					}
				}else{
					return new Msg(false,"无权操作");
				}
			}
		}
		return new Msg(false,"无权操作");
	}
	
	
	/**
	 * 撤销申请
	 */
	@Override
	public Msg delete(UserForm form) {
		// TODO Auto-generated method stub
		try{
		CircuicardEntity c=this.circuicardDao.load(CircuicardEntity.class, form.getId());
		if(c.getName().equals(WebContextUtil.getCurrentUser().getUser().getName())){
			if(c.getApplyStatus()==4){
				return new Msg(false,"已申请撤销，不可重复");
			}
			if(c.getOrgChargeApprove()==1){
				return new Msg(false,"已审批，不可注销,请联系上级或HR");
			}
			c.setApplyStatus(4);
			this.circuicardDao.update(c);
			return new Msg(true,"撤销成功！");
		}else{
			return new Msg(true,"无权撤销！");
		}
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"撤销失败！");
		}
	}
	
	/**
	 * 获取当前用户的角色默认值
	 */
	public int roleDefault(){
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
	
}
