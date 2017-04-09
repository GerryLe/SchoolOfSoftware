package com.rosense.module.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.OrgEntity;
import com.rosense.module.system.entity.PositionEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IOrgService;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.PositionForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

/**
 * @author Can-Dao
 * 	
 * 2016年7月23日 下午9:12:15
 * 
 */
@Service("orgService")
@Transactional
public class OrgService extends BaseService implements IOrgService {
	@Inject
	private IBaseDao<OrgEntity> basedaoOrg;
	@Inject
	private IBaseDao<UserEntity> userDao;
	
	public Msg add(OrgForm form) {
		if (this.getByName(form.getName()) != null)
			throw new ServiceException("该机构或部门的名称[" + form.getName() + "]已存在，无法添加！");
		final OrgEntity org = new OrgEntity();
		BeanUtils.copyNotNullProperties(form, org);
		org.setSort(WebContextUtil.getNextId("org"));
		if (StringUtil.isNotEmpty(form.getPid())) {
			org.setPid(form.getPid());
		}else{
			org.setPid(null);
		}

		this.basedaoOrg.add(org);
		this.logService.add("添加部门", "名称：[" + org.getName() + "]");
		form.setId(org.getId());
		return new Msg(true, "添加成功！");
	}

	
	public OrgForm getByName(String name) {
		List<OrgEntity> list = this.basedaoOrg.list("from OrgEntity where name=?", name);
		if (list != null && list.size() > 0) {
			OrgForm orgForm = new OrgForm();
			BeanUtils.copyNotNullProperties(list.get(0), orgForm);
			return orgForm;
		}
		return null;
	}

	
	public Msg delete(OrgForm form) {
		OrgEntity entity = this.basedaoOrg.load(OrgEntity.class, form.getId());
		del(entity);
		return new Msg(true, "删除成功！");
	}

	private void del(OrgEntity org) {
		List<OrgEntity> list = this.basedaoOrg.list("from OrgEntity where pid=?", org.getId());
		for (OrgEntity e : list) {
			del(e);
		}
		this.basedaoOrg.delete(OrgEntity.class, org.getId());
		this.basedaoOrg.executeSQL("update simple_person set orgId=? where orgId=?", new Object[] { null, org.getId() });
		this.logService.add("删除部门", "名称：[" + org.getName() + "]");
	}

	
	public Msg update(OrgForm form) {
		OrgEntity org = this.basedaoOrg.load(OrgEntity.class, form.getId());
		BeanUtils.copyNotNullProperties(form, org);
		if (null != form.getPid() && !"".equals(form.getPid())) {
			if (!org.getId().equals(form.getPid())) {
				org.setPid(form.getPid());
			} 
			else {
				return new Msg(false, "操作有误，父模块服务关联自己！");
			}
			
		}
		else {
			 org.setPid(null);
			}
		this.basedaoOrg.update(org);
		//System.out.println(form.getPid());
		//System.out.println(form.getId());
		this.logService.add("修改部门", "名称：[" + org.getName() + "]");
		return new Msg(true, "修改成功！");
	}

	
	public OrgForm get(OrgForm form) {
		OrgEntity entity = this.basedaoOrg.load(OrgEntity.class, form.getId());
		BeanUtils.copyNotNullProperties(entity, form);
		form.setPid(entity.getPid());
		return form;
	}

	
	public List<OrgForm> tree(String pid) {
		String sql="";
		if (null != pid && !"".equals(pid.trim()))
			sql = "select t.* from simple_org t where t.id='"+ pid +"' order by t.sort asc";
		else{
		    sql = "select t.* from simple_org t where t.pid is null order by t.sort asc";
		}
		List<OrgForm> list = this.basedaoOrg.listSQL(sql, OrgForm.class, false);
		List<OrgForm> forms = new ArrayList<OrgForm>();
		for (OrgForm e : list) {
			forms.add(recursive(e));
		}
		return forms;
	}

	public OrgForm recursive(OrgForm form) {
		form.setText(form.getName());
		List<OrgForm> orgs = this.basedaoOrg.listSQL("select t.* from simple_org t where t.pid='" + form.getId() + "' order by t.sort asc", OrgForm.class, false);
		if (null != orgs && orgs.size() > 0) {
			List<OrgForm> chlds = new ArrayList<OrgForm>();
			for (OrgForm e : orgs) {
				OrgForm recursive = this.recursive(e);

				OrgForm childform = new OrgForm();
				BeanUtils.copyNotNullProperties(recursive, childform);
				chlds.add(childform);
			}
			form.setChildren(chlds);
		}
		return form;
	}
	
	
	public OrgForm getId(String orgName) {
		String sql = "select t.* from simple_org t where t.name='" + orgName + "'";
		List<OrgForm> list = this.basedaoOrg.listSQL(sql, OrgForm.class, false);
		OrgForm of=new OrgForm();
		if (null != list && list.size() > 0) {
			for (OrgForm e : list) {
				 of=e;
			}
		}
		return of;
	}

	/**
	 * 查询各部门及子部门的员工
	 */
	public String userAndOrgtree(String pid) {
		String result="";
		String sql = "select t.* from simple_org t where t.pid is null order by t.sort asc";
		List<OrgForm> list = this.basedaoOrg.listSQL(sql, OrgForm.class, false);
		for (OrgForm e : list) {
			String MGTName="";
			String MGTChinaNamre="";
			//String userMGT = "select e.* from simple_person e left join simple_org o ON(e.orgId=o.id) where e.genre='MGT' and e.orgId='" + e.getId() + "'";
			//String userMGT = "select e.*,p.name positionName from simple_person e left join simple_org o ON(e.orgId=o.id) left join simple_position p ON(e.positionId=p.id) where p.name='MGT' and e.orgId='" + e.getId() + "'";
			String userMGT = "select e.*,r.defaultRole,r.sn from simple_person e left join simple_org o ON(e.orgId=o.id) left join  "
					+ "simple_user u ON(u.personId=e.id) left join simple_user_roles ur ON(u.id=ur.userId) "
					+ "left join  simple_role r ON(ur.roleId=r.id) where r.defaultRole='2' and r.sn='CHARGE' and e.orgChildId='" + e.getId() + "'";
			List<UserForm> listMGT = this.userDao.listSQL(userMGT, UserForm.class, false);
			if(listMGT!=null&&listMGT.size()>0){
			for(UserForm user : listMGT){
				MGTName+=user.getName()+" ";
				MGTChinaNamre+=user.getChinaname()+" ";
				}
			}
			result += "{" + "id : '" + e.getId() + "'" + ", name : '" + e.getName()+"  "+MGTName+" "+MGTChinaNamre + "'" + ", iconCls : '" + e.getIconCls() + "'"
					+ ", sort : '" + e.getSort() + "'" + ", pid : '" + e.getPid() + "'";
				result += ", children : ";
			String sqluser = "select e.* from simple_person e left join simple_org o ON(e.orgId=o.id) where e.orgChildId='" + e.getId() + "'";
			List<UserForm> userlist = this.userDao.listSQL(sqluser, UserForm.class, false);
			result+="[";
			if(userlist!=null&&userlist.size()>0){
				for(UserForm user : userlist){
					result += toUserString(e,user);
					}
			}	
			result += childrentoString(e.getId());
			result+="]";
			result += "},";
		}
		
		result=result.substring(0, result.length() - 1);
		return result;
	}

	private String childrentoString(String id) {
		List<OrgForm> orgs = this.basedaoOrg.listSQL("select t.* from simple_org t where t.pid='" + id + "' order by t.sort asc", OrgForm.class, false);
		String result = "";
		if (null != orgs && orgs.size() > 0) {
			for (OrgForm e : orgs) {
				String MGTName="";
				String MGTChinaNamre="";
				//String userMGT = "select e.*,p.name positionName from simple_person e left join simple_org o ON(e.orgId=o.id) left join simple_position p ON(e.positionId=p.id) where p.name='MGT' and e.orgId='" + e.getId() + "'";
				String userMGT = "select e.*,r.defaultRole,r.sn from simple_person e left join simple_org o ON(e.orgId=o.id) left join  simple_user u ON(u.personId=e.id) left join simple_user_roles ur ON(u.id=ur.userId) left join  simple_role r ON(ur.roleId=r.id) where r.defaultRole='2' and r.sn='CHARGE' and e.orgChildId='" + e.getId() + "'";
				List<UserForm> listMGT = this.userDao.listSQL(userMGT, UserForm.class, false);
				if(listMGT!=null&&listMGT.size()>0){
				for(UserForm user : listMGT){
					MGTName+=user.getName()+" ";
					MGTChinaNamre+=user.getChinaname()+" ";
					}
				}
				result += "{" + "id : '" + e.getId() + "'" + ", name : '" + e.getName()+"  "+MGTName+" "+MGTChinaNamre+ "'" + ", iconCls : '" + e.getIconCls() + "'"
						+ ", sort : '" + e.getSort() + "'" + ", pid : '" + e.getPid() + "'";
					result += ", children : ";
				
				String sql = "select e.* from simple_person e left join simple_org o ON(e.orgId=o.id) where e.orgChildId='" + e.getId() + "'";
				List<UserForm> list = this.userDao.listSQL(sql, UserForm.class, false);
				result+="[";
				if(list!=null&&list.size()>0){
					
					for(UserForm user : list){
						result += toUserString(e,user);
						}
				    }
					result += childrentoString(e.getId());
					result+="]";
				result+="},";
			}
			result = result.substring(0, result.length() - 1);
			return result;
		}else{
		return result+="";
		}
	}

	private String toUserString(OrgForm e,UserForm user) {
		String result = "{" + "id : '" + user.getId() + "'" + ", name : '" + user.getName()+"  "+user.getChinaname()+ "'" + ", account : '" + user.getAccount() + "'"
				+ ", sex : '" + user.getSex() + "'" + ", pid : '" + e.getId()+ "'";
		return result + "},";
	}


	@Override
	public String getName(String id) {
		// TODO Auto-generated method stub
		String name;
		String hql = "from OrgEntity o where o.id='" + id + "'";
		List<OrgEntity> list = this.basedaoOrg.list(hql);
		name=list.get(0).getName();
		return name;
	}
	
	
	
}
