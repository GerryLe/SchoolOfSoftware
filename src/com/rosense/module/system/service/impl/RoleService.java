package com.rosense.module.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.ClobUtil;
import com.rosense.basic.util.PinyinUtil;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.dbutil.IDBUtilsHelper;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.service.IRoleService;
import com.rosense.module.system.web.form.RoleForm;

@Service("roleService")
@Transactional
public class RoleService extends BaseService implements IRoleService {
	private Logger logger = Logger.getLogger(RoleService.class);
	@Inject
	private IBaseDao<RoleEntity> roleDao;
	@Inject
	private IDBUtilsHelper dbutil;

	
	public Msg add(RoleForm form) {
		try {
			if (this.getBySn(form.getSn()) != null)
				return new Msg(false, "该的角色的SN[" + form.getSn() + "]已存在，无法添加！");
			if (StringUtil.isEmpty(form.getSn())) {
				form.setSn(PinyinUtil.getPinYin(form.getName()));
			}
			//修改默认角色
			if (form.getDefaultRole() != 1) {
				String org="select r.* from simple_role r where 1=1 ORDER BY r.defaultRole desc LIMIT 1";
				RoleForm orgform=(RoleForm)this.roleDao.queryObjectSQL(org, RoleForm.class,false);
				if(orgform!=null){
					form.setDefaultRole(orgform.getDefaultRole()+1);
				}
				
				//this.dbutil.getQr().update("update simple_role set defaultRole=0 where defaultRole=1");
			}

			RoleEntity role = new RoleEntity();
			BeanUtils.copyNotNullProperties(form, role, new String[] { "remark" });
			role.setRemark(ClobUtil.getClob(form.getRemark()));
			role.setCreated(new Date());
			this.roleDao.add(role);
			this.logService.add("添加角色", "名称：[" + form.getName() + "]");
			form.setId(role.getId());
			return new Msg(true, "添加成功！");
		} catch (Throwable e) {
			logger.error("添加角色信息失败===>异常信息：", e);
			return new Msg(false, "添加失败！");
		}
	}

	
	public Msg delete(RoleForm form) {
		try {
			if (StringUtils.isNotBlank(form.getId())) {
				RoleEntity role = this.roleDao.load(RoleEntity.class, form.getId());
				if ("ADMIN".equals(role.getSn())) {
					throw new Exception("内置角色，不能删除");
				}
				this.roleDao.delete(role);
				this.logService.add("删除角色", "名称：[" + role.getName() + "]");
				return new Msg(true, "删除成功！");
			}
		} catch (Exception e) {
			logger.error("根据ID[" + form.getIds() + "]删除角色信息失败===>异常信息：", e);
			return new Msg(false, "删除失败==>" + e.getMessage());
		}
		return null;
	}

	
	public Msg update(RoleForm form) {
		try {
			//修改默认角色
			/*if (form.getDefaultRole() == 1) {
				//this.dbutil.getQr().update("update simple_role set defaultRole=0 where defaultRole=1");
				
			}*/
            
			RoleEntity role = this.roleDao.load(RoleEntity.class, form.getId());
		    role.setName(form.getName());
			//BeanUtils.copyNotNullProperties(form, role, new String[] { "remark", "sn" });
			role.setRemark(ClobUtil.getClob(form.getRemark()));
			this.roleDao.update(role);
			this.logService.add("修改角色", "名称：[" + role.getName() + "]");
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			logger.error("修改角色信息失败===>异常信息：", e);
			return new Msg(false, "修改角色信息失败！");
		}
	}

	
	public RoleForm getByName(String name) {
		List<RoleEntity> list = this.roleDao.list("from RoleEntity where name=?", name);
		if (list != null && list.size() > 0) {
			RoleForm orgForm = new RoleForm();
			BeanUtils.copyNotNullProperties(list.get(0), orgForm);
			return orgForm;
		}
		return null;
	}

	
	public RoleForm get(RoleForm form) {
		try {
			RoleEntity entity = this.roleDao.load(RoleEntity.class, form.getId());
			BeanUtils.copyNotNullProperties(entity, form, new String[] { "remark" });
			form.setRemark(ClobUtil.getString(entity.getRemark()));
			return form;
		} catch (Exception e) {
			logger.error("加载角色信息失败===>异常信息：", e);
		}
		return null;
	}

	
	public DataGrid datagrid(RoleForm form) {
		try {
			List<RoleForm> forms = new ArrayList<RoleForm>();
			Pager<RoleForm> pager = this.find(form);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (RoleForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			logger.error("加载角色列表信息失败===>异常信息：", e);
		}
		return null;
	}

	private Pager<RoleForm> find(RoleForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
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
		
		String sql="";
		//非管理员无法选择超级管理员角色进行授权
		if(roleId.contains(0)&&roleName.contains("ADMIN")){
			 sql = "select t.* from simple_role t where 1=1 ";
		}else{
		     sql = "select t.* from simple_role t where name !='超级管理员' and  sn!='ADMIN'";
		}
		sql = addWhere(sql, form, alias);

		return this.roleDao.findSQL(sql, alias, RoleForm.class, false);
	}

	private String addWhere(String sql, RoleForm form, Map<String, Object> params) {
		if (null != form) {
			if (form.getSn() != null && !"".equals(form.getSn().trim())) {
				sql += " and t.sn like :sn";
				params.put("sn", "%%" + form.getSn() + "%%");
			}
			if (form.getName() != null && !"".equals(form.getName().trim())) {
				sql += " and t.name like :name";
				params.put("name", "%%" + form.getName() + "%%");
			}
		}
		return sql;
	}

	
	public RoleForm getRoleMenus(RoleForm form) {
		String sql = "select t.menu_id from simple_role_menus t where t.role_id=?";

		List<Object[]> menuIds = this.roleDao.listSQL(sql, form.getId());
		if (null != menuIds && menuIds.size() > 0) {
			form.setMenu_ids(StringUtils.join(menuIds.toArray(), ","));
		}
		return form;
	}

	private String getBySn(String sn) {
		return (String) this.roleDao.queryObject("select t.sn from RoleEntity t where t.sn=?", new Object[] { sn });
	}

	
	public void init(RoleEntity entity) {
		if (this.getBySn(entity.getSn()) != null)
			throw new ServiceException("该的角色的SN[" + entity.getName() + "-" + entity.getSn() + "]已存在，无法添加！");

		entity.setCreated(new Date());
		this.roleDao.add(entity);
	}

	/**
	 * 根据名称获取角色Id值
	 */
	public String getRoleId(String name){
		 String ids="";
		 String sql="select r.id from simple_role r where r.name='"+name+"'";
  		 RoleForm form=(RoleForm) this.roleDao.queryObjectSQL(sql,RoleForm.class,false);
		 if(form!=null){
         		ids=form.getId();
         	}
		 return ids;
	}
}
