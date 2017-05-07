package com.rosense.module.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.ClassEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IClassService;
import com.rosense.module.system.web.form.ClassForm;
import com.rosense.module.system.web.form.UserForm;

/**
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Service("classService")
@Transactional
public class ClassService extends BaseService implements IClassService {
	@Inject
	private IBaseDao<ClassEntity> basedaoClass;
	@Inject
	private IBaseDao<UserEntity> userDao;
	
	public Msg add(ClassForm form) {
		if (this.getByName(form.getClass_name()) != null)
			throw new ServiceException("该机构或部门的名称[" + form.getClass_name() + "]已存在，无法添加！");
		final ClassEntity clas = new ClassEntity();
		BeanUtils.copyNotNullProperties(form, clas);
		clas.setSort(WebContextUtil.getNextId("class"));
		if (StringUtil.isNotEmpty(form.getPid())) {
			clas.setPid(form.getPid());
		}else{
			clas.setPid(null);
		}

		this.basedaoClass.add(clas);
		this.logService.add("添加部门", "名称：[" + clas.getClass_name() + "]");
		form.setId(clas.getId());
		return new Msg(true, "添加成功！");
	}

	
	public ClassForm getByName(String name) {
		List<ClassEntity> list = this.basedaoClass.list("from ClassEntity where class_name=?", name);
		if (list != null && list.size() > 0) {
			ClassForm classForm = new ClassForm();
			BeanUtils.copyNotNullProperties(list.get(0), classForm);
			return classForm;
		}
		return null;
	}

	
	public Msg delete(ClassForm form) {
		ClassEntity entity = this.basedaoClass.load(ClassEntity.class, form.getId());
		del(entity);
		return new Msg(true, "删除成功！");
	}

	private void del(ClassEntity org) {
		List<ClassEntity> list = this.basedaoClass.list("from ClassEntity where pid=?", org.getId());
		for (ClassEntity e : list) {
			del(e);
		}
		this.basedaoClass.delete(ClassEntity.class, org.getId());
		//this.basedaoClass.executeSQL("update simple_student set classId=? where classId=?", new Object[] { null, org.getId() });
		this.logService.add("删除班级", "名称：[" + org.getClass_name() + "]");
	}

	
	public Msg update(ClassForm form) {
		ClassEntity org = this.basedaoClass.load(ClassEntity.class, form.getId());
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
		this.basedaoClass.update(org);
		this.logService.add("修改部门", "名称：[" + org.getClass_name() + "]");
		return new Msg(true, "修改成功！");
	}

	
	public ClassForm get(ClassForm form) {
		ClassEntity entity = this.basedaoClass.load(ClassEntity.class, form.getId());
		BeanUtils.copyNotNullProperties(entity, form);
		form.setPid(entity.getPid());
		return form;
	}

	public List<ClassForm> pidtree(String pid) {
		String sql = "select t.* from simple_class t where t.pid is null order by t.sort asc";
		List<ClassForm> list = this.basedaoClass.listSQL(sql, ClassForm.class, false);
		return list;
	}
	
	public List<ClassForm> tree(String pid) {
		String sql="";
		if (null != pid && !"".equals(pid.trim()))
			sql = "select t.* from simple_class t where t.id='"+ pid +"' order by t.sort asc";
		else{
		    sql = "select t.* from simple_class t where t.pid is null order by t.sort asc";
		}
		List<ClassForm> list = this.basedaoClass.listSQL(sql, ClassForm.class, false);
		List<ClassForm> forms = new ArrayList<ClassForm>();
		if(list!=null&&list.size()>0){
			for (ClassForm e : list) {
				forms.add(recursive(e));
			}
		}
		return forms;
	}
	
	public List<ClassForm> treeChild(String pid) {
		String sql="";
		if (null != pid && !"".equals(pid.trim()))
			sql = "select t.* from simple_class t where t.pid='"+ pid +"' order by t.sort asc";
		else{
		    sql = "select t.* from simple_class t where t.pid is null order by t.sort asc";
		}
		List<ClassForm> list = this.basedaoClass.listSQL(sql, ClassForm.class, false);
		List<ClassForm> forms = new ArrayList<ClassForm>();
		if(list!=null&&list.size()>0){
			for (ClassForm e : list) {
				forms.add(recursive(e));
			}
		}
		return forms;
	}

	public ClassForm recursive(ClassForm form) {
		form.setText(form.getClass_name());
		List<ClassForm> orgs = this.basedaoClass.listSQL("select t.* from simple_class t where t.pid='" + form.getId() + "' order by t.sort asc", ClassForm.class, false);
		if (null != orgs && orgs.size() > 0) {
			List<ClassForm> chlds = new ArrayList<ClassForm>();
			for (ClassForm e : orgs) {
				ClassForm recursive = this.recursive(e);

				ClassForm childform = new ClassForm();
				BeanUtils.copyNotNullProperties(recursive, childform);
				chlds.add(childform);
			}
			form.setChildren(chlds);
		}
		return form;
	}
	
	
	public ClassForm getId(String className) {
		String sql = "select c.* from simple_class c where c.class_name='" + className + "'";
		List<ClassForm> list = this.basedaoClass.listSQL(sql, ClassForm.class, false);
		ClassForm cf=new ClassForm();
		if (null != list && list.size() > 0) {
			for (ClassForm e : list) {
				 cf=e;
			}
		}
		return cf;
	}

	/**
	 * 查询各部门及子部门的员工
	 */
	public String userAndOrgtree(String pid) {
		String result="";
		String sql = "select t.* from simple_org t where t.pid is null order by t.sort asc";
		List<ClassForm> list = this.basedaoClass.listSQL(sql, ClassForm.class, false);
		for (ClassForm e : list) {
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
			result += "{" + "id : '" + e.getId() + "'" + ", name : '" + e.getClass_name()+"  "+MGTName+" "+MGTChinaNamre + "'" + ", iconCls : '" + e.getIconCls() + "'"
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
		List<ClassForm> orgs = this.basedaoClass.listSQL("select t.* from simple_org t where t.pid='" + id + "' order by t.sort asc", ClassForm.class, false);
		String result = "";
		if (null != orgs && orgs.size() > 0) {
			for (ClassForm e : orgs) {
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
				result += "{" + "id : '" + e.getId() + "'" + ", name : '" + e.getClass_name()+"  "+MGTName+" "+MGTChinaNamre+ "'" + ", iconCls : '" + e.getIconCls() + "'"
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

	private String toUserString(ClassForm e,UserForm user) {
		String result = "{" + "id : '" + user.getId() + "'" + ", name : '" + user.getName()+"  "+user.getChinaname()+ "'" + ", account : '" + user.getAccount() + "'"
				+ ", sex : '" + user.getSex() + "'" + ", pid : '" + e.getId()+ "'";
		return result + "},";
	}


	@Override
	public String getName(String id) {
		// TODO Auto-generated method stub
		String name;
		String hql = "from OrgEntity o where o.id='" + id + "'";
		List<ClassEntity> list = this.basedaoClass.list(hql);
		name=list.get(0).getClass_name();
		return name;
	}
	
	/**
	 * 获取班级信息
	 */
    public DataGrid datagridClass(ClassForm form){
    	try{
    		List<ClassForm> forms = new ArrayList<ClassForm>();
    		Map<String, Object> alias = new HashMap<String, Object>();
	    	String sql="select * from simple_class where 1=1";
	    	Pager<ClassForm> pager = this.basedaoClass.findSQL(sql, alias, ClassForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (ClassForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("加载列表信息异常：", e);
		}
    }
	
	
}
