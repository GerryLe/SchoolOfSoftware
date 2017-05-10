package com.rosense.module.system.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.FreemarkerUtil;
import com.rosense.basic.util.MD5Util;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.cache.Caches;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.PermitsMenuEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.StudentEntity;
import com.rosense.module.system.entity.TeacherEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IUserService;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

import net.sf.json.JSONObject;


@Service("userService")
@Transactional
public class UserService extends BaseService implements IUserService {
	private Logger logger = Logger.getLogger(UserService.class);
	@Inject
	private IBaseDao<UserEntity> userDao;
	@Inject
	private IBaseDao<StudentEntity> studentDao;
	@Inject
	private IBaseDao<TeacherEntity> teacherDao;;
	@Inject
	private IBaseDao<RoleEntity> roleDao;
	@Inject
	private IBaseDao<PermitsMenuEntity> permitsMenuDao;

	private FreemarkerUtil futil;

	@Autowired(required = true)
	public UserService(String ftlPath, String outPath) {
		if (null == futil) {
			futil = FreemarkerUtil.getInstance(ftlPath);
		}
	}
	
	public Msg delete(UserForm form) {
		try {
			if (null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",");
				for (String id : ids) {
					String sqlRole="select r.* from simple_user_roles ru left join simple_role r on(ru.roleId=r.id)";
					sqlRole+=" left join simple_user u on(ru.userId=u.id)";
					sqlRole+=" where u.id='"+id+"'";
					RoleForm role=(RoleForm) this.roleDao.queryObjectSQL(sqlRole,RoleForm.class,false);
					UserEntity user = this.userDao.load(UserEntity.class, id);
					if ("admin".equals(user.getAccount())) {
						return new Msg(false, "删除失败,不能删除admin用户！");
					}
					if(role.getDefaultRole()==4){
						this.studentDao.delete(StudentEntity.class, user.getPersonId());
					}else{
						this.teacherDao.delete(TeacherEntity.class, user.getPersonId());
					}
					this.userDao.delete(user);

					this.logService.add("删除用户", "账号：[" + user.getAccount() + "]");
				}
			}
			return new Msg(true, "删除成功！");
		} catch (Exception e) {
			logger.error("根据ID[" + form.getIds() + "]删除用户信息失败===>异常信息：", e);
			return new Msg(false, "删除失败！");
		}
	}
	// 获取信息
		public UserForm get(String id) {

			try {
				String sql = "select u.*  from simple_user u ";
				sql += " where u.id=?";
				UserForm form = (UserForm) this.userDao.queryObjectSQL(sql, new Object[] { id }, UserForm.class, false);
				if (form != null) {
					String role_sql = "SELECT ur.roleId FROM simple_user_roles ur WHERE ur.userId=?";
					List<Object[]> roleIds = this.userDao.listSQL(role_sql, form.getId());
					if (null != roleIds && roleIds.size() > 0)
						form.setRole_ids(org.apache.commons.lang3.StringUtils.join(roleIds.toArray(), ","));
				}
				return form;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("加载信息失败===>异常信息：", e);
				throw new ServiceException("加载用户信息异常：", e);
			}
		}
	
	public DataGrid datagrid(UserForm form,String selectType,String searchKeyName) {
		if (null == form.getSort()) {
			SystemContext.setSort("u.status desc,u.created");
			SystemContext.setOrder("desc");
		} else {
			SystemContext.setSort("u." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		try {
			List<UserForm> forms = new ArrayList<UserForm>();
			Pager<UserForm> pager = this.find(form,selectType,searchKeyName);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (UserForm pf : pager.getDataRows()) {
					if (null != pf.getId()) {
						// 获取角色
						List<RoleForm> roles = this.roleDao.listSQL(
								"select r.name from simple_user_roles t LEFT JOIN simple_role r on(r.id=t.roleId) WHERE t.userId=?",
								new Object[] { pf.getId() }, RoleForm.class, false);
						if (null != roles) {
							StringBuffer s = new StringBuffer();
							for (RoleForm r : roles) {
								s.append(r.getName() + ",");
							}
							pf.setRole_names((s.length() > 0 ? s.deleteCharAt(s.length() - 1).toString() : ""));
						}
					} else {
						pf.setStatus(2);
					}
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载列表信息失败===>异常信息：", e);
			throw new ServiceException("加载列表信息异常：", e);
		}
	}

	private Pager<UserForm> find(UserForm form,String selectType,String searchKeyName ) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select u.* from simple_user u  where 1=1 ";
		sql = addWhere(sql, form, alias);
		return this.userDao.findSQL(sql, alias, UserForm.class, false);
	}

	private String addWhere(String sql, UserForm form, Map<String, Object> params) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if (key.equals("phone")) {
					form.setPhone(jsonObject.get(key).toString());
				}
				if (key.equals("name")) {
					form.setName(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if (key.equals("area")) {
					form.setArea(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
			}
		}
		if (StringUtil.isNotEmpty(form.getName())) {
			try {
				params.put("name", "%%" + URLDecoder.decode(form.getName(), "UTF-8") + "%%");
				sql += " and e.name like :name";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getPhone())) {
			try {
				params.put("phone", "%%" + form.getPhone() + "%%");
				sql += " and e.phone like :phone";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getArea())) {
			try {
				params.put("area", "%%" + form.getArea() + "%%");
				sql += " and e.area like :area";
			} catch (Exception e) {
			}
		}
		
		if (StringUtil.isNotEmpty(form.getSex())) {
			sql += " and e.sex= :sex ";
			params.put("sex", form.getSex());
		}
		if (StringUtil.isNotEmpty(form.getEmail())) {
			sql += " and e.email like :email ";
			params.put("email", "%%" + form.getEmail() + "%%");
		}
		if (StringUtil.isNotEmpty(form.getAccount())) {
			sql += " and u.account like :account ";
			params.put("account", "%%" + form.getAccount() + "%%");
		}
		if (StringUtil.isNotEmpty(form.getSelectType())) {
			try {
				if(form.getSelectType().equals("account")){
					sql += " and u.account like '%"+new String(form.getSearchKeyName().getBytes("ISO-8859-1"),"UTF-8")+"%' ";
				}else{
					sql += " and u.name like '%"+new String(form.getSearchKeyName().getBytes("ISO-8859-1"),"UTF-8")+"%' ";
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sql;
	}

	public Msg resetPwd(String id) {
		try {
			if (StringUtil.isNotEmpty(id)) {
				UserEntity user = this.userDao.load(UserEntity.class, id);
				user.setPassword(MD5Util.md5(Const.DEFAULTPASS));
				user.setCreated(new Date());
				this.userDao.update(user);
				this.logService.add("重置密码", "账号：[" + user.getAccount() + "]");
				return new Msg(true, "重设密码成功！");
			} else {
				return new Msg(false, "重设密码失败！");
			}
		} catch (ServiceException e) {
			return new Msg(false, "重设密码失败！");
		}
	}

	public Msg lockUser(String id) {
		try {
			if (StringUtil.isNotEmpty(id)) {
				UserEntity user = this.userDao.load(UserEntity.class, id);
				if ("admin".equals(user.getAccount())) {
					return new Msg(false, "演示账号不能锁定！");
				}
				if (user.getStatus() == 0) {
					user.setStatus(1);
					this.logService.add("账号锁定", "账号：[" + user.getAccount() + "]");
				} else {
					this.logService.add("账号解锁", "账号：[" + user.getAccount() + "]");
					user.setStatus(0);
				}
				this.userDao.update(user);
				if (user.getStatus() == 0) {
					return new Msg(true, "账号已锁定！");
				} else if (user.getStatus() == 1) {
					return new Msg(true, "账号已解锁！");
				}
			}
		} catch (ServiceException e) {
			return new Msg(false, "程序发送错误！");
		}

		return new Msg(true, "出现错误！");
	}


	public Msg batchUserRole(UserForm form) {
		try {
			if (StringUtil.isNotEmpty(form.getId())) {
				Set<RoleEntity> roles = new HashSet<RoleEntity>();
				if (null != form.getRole_ids() && !"".equals(form.getRole_ids())) {
					String[] ids = form.getRole_ids().split(",");
					for (String id : ids) {
						roles.add(this.roleDao.load(RoleEntity.class, id));
					}
				}

				UserEntity user = this.userDao.load(UserEntity.class, form.getId());
				user.setRoles(roles);
				this.userDao.update(user);
				this.logService.add("修改角色", "账号：[" + user.getAccount() + "]");
				return new Msg(true, "加入角色成功！");
			} else {
				return new Msg(false, "加入角色失败！");
			}
		} catch (BeansException e) {
			return new Msg(false, "批量加入角色失败！");
		}
	}

	public Msg updatePwd(UserForm form) {
		final UserEntity user = this.userDao.load(UserEntity.class, WebContextUtil.getUserId());
		if (!user.getPassword().equals(MD5Util.md5(form.getOldPwd()))) {
			return new Msg(false, "旧密码不正确");
		}
		if ("admin".equals(user.getAccount())) {
			return new Msg(false, "Demo系统，无法修改admin密码");
		}
		user.setPassword(MD5Util.md5(form.getPassword()));
		this.userDao.update(user);
		this.logService.add("修改密码", "账号：[" + user.getAccount() + "]");
		return new Msg(true, "密码修改成功");
	}

	public Msg updatePhoto(final String photo) {
		try {
			final LoginUser user = WebContextUtil.getCurrentUser().getUser();
			if(WebContextUtil.getCurrentUser().getUser().getDefaultRole()==4){
				StudentEntity entity = this.studentDao.load(StudentEntity.class, user.getPersonId());
				entity.setPhoto(photo);
				this.studentDao.update(entity);
			}else{
				TeacherEntity teacher=this.teacherDao.load(TeacherEntity.class, user.getPersonId());
				teacher.setPhoto(photo);
				this.teacherDao.update(teacher);
			}
			user.setPhoto(photo);
			Map<String, String> map = Caches.getMap("USER", user.getUserId());
			map.put("PHOTO", photo);
			Caches.set("USER", user.getUserId(), map);
			this.logService.add("上传照片", "账号：[" + user.getAccount() + "]");
		} catch (Exception e) {
		}
		return new Msg(true);
	}

}
