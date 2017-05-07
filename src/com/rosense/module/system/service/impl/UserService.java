package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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

/**
 * 删除用户操作，只删除用户账号的数据，用户对于的员工信息见不会删除， 员工删除操作需员工管理页面下进行操作
 *
 */
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
	/*public List<UserForm> searchUsers(String name) {
		try {
			name = URLDecoder.decode(name, "utf-8");
			String sql = "select u.* from simple_user u where u.name like '%" + name + "%' or u.account like '%" + name
					+ "%'";
			return this.userDao.listSQL(sql, UserForm.class, false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载人员信息失败===>异常信息：", e);
			throw new ServiceException("加载用户信息异常：", e);
		}
	}*/

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
					//this.huDao.delete(HolidaysUsersEntity.class, user.getHolidaysId());
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
				String sql = "select u.*,e.locateid,e.callerid,e.chinaname,e.email,e.sex,e.employmentStr,e.phone,e.job,e.province, e.orgId, e.positionId, e.becomeStaffDate,e.birthday,e.securityDate,e.school,e.profession,e.graduation, e.degree,e.accountAddr,e.accountPro,e.address,e.age,e.workAge,e.probationLimit,e.probationEnd,e.marriage,e.agreementEndDate,e.agreementLimit,e.positionEng,e.orgChildId,e.agreementStartDate,e.agreementTimes,e.area,e.workOld,e.material,e.bankCard,e.bear,e.idcard,e.nation,e.origin,e.train,e.securityCard,e.politicalFace,e.certificate,e.contact,e.contactPhone,e.fund,e.fundDate from simple_user u ";
				sql += " left join simple_person e ON(u.personId=e.id) ";
				sql += " where u.id=?";
				UserForm form = (UserForm) this.userDao.queryObjectSQL(sql, new Object[] { id }, UserForm.class, false);
				if (form != null) {
					String role_sql = "SELECT ur.roleId FROM simple_user_roles ur WHERE ur.userId=?";
					List<Object[]> roleIds = this.userDao.listSQL(role_sql, form.getId());
					if (null != roleIds && roleIds.size() > 0)
						form.setRole_ids(org.apache.commons.lang3.StringUtils.join(roleIds.toArray(), ","));
				}
				if (!StringUtil.isEmpty(form.getEmploymentStr())) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
					String[] fromDate = form.getEmploymentStr().split("/");
					String[] toDate = df.format(new Date()).split("/");
					form.setWorkAge((Integer.parseInt(toDate[0]) - Integer.parseInt(fromDate[0])) + "年");
				}
				return form;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("加载人员信息失败===>异常信息：", e);
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
		String sql = "select u.*, e.sex, e.phone, e.email from simple_user u left join simple_person e ON(e.id=u.personId)  where 1=1 ";
		if(StringUtil.isNotEmpty(searchKeyName)){
			sql=addWhereSearch(sql, form, alias,selectType,searchKeyName);
		}else{
		   sql = addWhere(sql, form, alias);
		}
		return this.userDao.findSQL(sql, alias, UserForm.class, false);
	}

	/**
	 * 信息筛选
	 * @param sql
	 * @param form
	 * @param params
	 * @param selectType
	 * @param searchKeyName
	 * @return
	 */
	private String addWhereSearch(String sql, UserForm form, Map<String, Object> params,String selectType,String searchKeyName) {
				if (selectType.equals("phone")) {
					try {
						//params.put("phone", "%%" + searchKeyName + "%%");
						sql += " and e.phone like '%"+searchKeyName+"%'";
					} catch (Exception e) {
					}
				}
				if (selectType.equals("name")) {
					try {
						//params.put("name", "%%" + URLDecoder.decode(searchKeyName, "UTF-8") + "%%");
						sql += " and e.name like '%"+searchKeyName+"%'";
					} catch (Exception e) {
					}
				}
				if (selectType.equals("chinaname")) {
					try {
						//params.put("chinaname", "%%" + URLDecoder.decode(searchKeyName, "UTF-8") + "%%");
						sql += " and e.chinaname like '%"+searchKeyName+"%'";
					} catch (Exception e) {
					}
				}
				if (selectType.equals("area")) {
					try {
						//params.put("area", "%%" + searchKeyName+ "%%");
						sql += " and e.area like '%"+searchKeyName+"%'";
					} catch (Exception e) {
					}
				}
				if (selectType.equals("account")) {
					try {
						//params.put("area", "%%" + searchKeyName+ "%%");
						sql += " and u.account like '%"+searchKeyName+"%'";
					} catch (Exception e) {
					}
				}
		return sql;
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
				if (key.equals("chinaname")) {
					form.setChinaname(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if (key.equals("job")) {
					form.setJob(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if (key.equals("area")) {
					form.setArea(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
			}
		}
		if (StringUtil.isNotEmpty(form.getChinaname())) {
			try {
				params.put("chinaname", "%%" + URLDecoder.decode(form.getChinaname(), "UTF-8") + "%%");
				sql += " and e.chinaname like :chinaname";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getJob())) {
			try {
				params.put("job", "%%" + URLDecoder.decode(form.getJob(), "UTF-8") + "%%");
				sql += " and e.job like :job";
			} catch (Exception e) {
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

	private int equlasVal(String param) {
		String sql = "select u.* from simple_user u where " + param;
		return this.userDao.countSQL(sql, false).intValue();
	}

	private String getUserId(String param) {
		String idString = "";
		String sql = "select u.* from simple_user u where u.account='" + param + "'";
		List<UserForm> list = this.userDao.listSQL(sql, UserForm.class, false);
		if (null != list && list.size() > 0) {
			for (UserForm e : list) {
				idString = e.getId();
			}
		}
		return idString;
	}

	private int equlasValByPerson(String param) {
		String sql = "select p.* from simple_person p where " + param;
		return this.studentDao.countSQL(sql, false).intValue();
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

	/*
	*//**
	 * 获取登陆用户的权限 如果同时拥有用户授权，角色授权，部门授权，岗位授权，则进行权限累加，并去除重复的权限
	 *//*

	public AuthForm getAuth(String userId) {
		AuthForm auth = new AuthForm();
		List<Object> tree = new ArrayList<Object>();
		List<ACLForm> opers = new ArrayList<ACLForm>();
		List<String> authUrl = new ArrayList<String>();

		// 获取用户和人员信息
		String sql = "select u.*,  e.orgId, e.orgChildId, e.positionId from simple_user u  left join simple_person e ON(e.id=u.personId)  where u.id=? ";
		UserForm user = (UserForm) this.userDao.queryObjectSQL(sql, userId, UserForm.class, false);

		// 获取用户的角色ID
		List<Object[]> roleIds = this.roleDao
				.listSQL("select r.userId, r.roleId from simple_user_roles r where r.userId=?", userId);

		*//********************************* 用户权限 **************************************//*
		// 获取用户权限
		List<ACLForm> aclUserMenus = getAclMenus(userId, Const.PRINCIPAL_USER);
		List<ACLForm> aclUserOpers = getAclOpers(userId, Const.PRINCIPAL_USER);
		initTreeAndOpers(tree, opers, authUrl, aclUserMenus, aclUserOpers);

		*//********************************* 角色权限 **************************************//*
		// 获取用户角色权限
		for (Object[] o : roleIds) {
			List<ACLForm> aclRoleMenus = getAclMenus((String) o[1], Const.PRINCIPAL_ROLE);
			List<ACLForm> aclRoleOpers = getAclOpers((String) o[1], Const.PRINCIPAL_ROLE);
			initTreeAndOpers(tree, opers, authUrl, aclRoleMenus, aclRoleOpers);
		}
		*//********************************** 部门权限 *************************************//*
		// 获取部门权限
		List<ACLForm> aclOrgMenus = getAclMenus(user.getOrgId(), Const.PRINCIPAL_DEPT);
		List<ACLForm> aclOrgOpers = getAclOpers(user.getOrgId(), Const.PRINCIPAL_DEPT);
		initTreeAndOpers(tree, opers, authUrl, aclOrgMenus, aclOrgOpers);
		*//*********************************** 岗位权限 ************************************//*
		// 获取岗位权限
		List<ACLForm> aclPositionMenus = getAclMenus(user.getPositionId(), Const.PRINCIPAL_POSITION);
		List<ACLForm> aclPositionOpers = getAclOpers(user.getPositionId(), Const.PRINCIPAL_POSITION);
		initTreeAndOpers(tree, opers, authUrl, aclPositionMenus, aclPositionOpers);

		auth.setAuthTree(tree);
		auth.setAuthOpers(opers);
		auth.setAuthUrl(authUrl);
		return auth;
	}

	private void initTreeAndOpers(List<Object> tree, List<ACLForm> opers, List<String> authUrl, List<ACLForm> aclMenus,
			List<ACLForm> aclOpers) {
		// 菜单
		for (ACLForm a : aclMenus) {
			Map<String, Object> dataRecord = new HashMap<String, Object>();
			dataRecord.put("id", a.getMenuId());
			dataRecord.put("text", a.getMenuName());
			dataRecord.put("href", a.getMenuHref());
			dataRecord.put("iconCls", a.getMenuIconCls());
			dataRecord.put("state", a.getState());
			dataRecord.put("pid", a.getMenuPid());
			dataRecord.put("weight", a.getMenuSort());
			dataRecord.put("color", a.getMenuColor());
			dataRecord.put("alias", a.getAlias());
			dataRecord.put("menuId", a.getMenuId());
			if (!tree.contains(dataRecord)) {
				tree.add(dataRecord);
			}
		}
		// 操作
		for (ACLForm aclForm : aclOpers) {
			opers.add(aclForm);
			authUrl.add(aclForm.getOperMenuHref());
		}
	}

	private List<ACLForm> getAclMenus(String principalId, String principalType) {
		String sql = "select t.* from simple_permits_menu t where t.principalId=? and t.principalType=? order by menusort";
		return this.permitsMenuDao.listSQL(sql, new Object[] { principalId, principalType }, ACLForm.class, false);
	}

	private List<ACLForm> getAclOpers(String principalId, String principalType) {
		String sql = "select t.* from simple_permits_oper t where t.principalId=? and t.principalType=?";
		return this.permitsMenuDao.listSQL(sql, new Object[] { principalId, principalType }, ACLForm.class, false);
	}

	public Msg addRoleForUser(String roleId, String users) {
		for (String userId : users.split(",")) {
			try {
				this.roleDao.executeSQL("insert into simple_user_roles values(?,?)", new Object[] { userId, roleId });
				UserEntity user = this.userDao.load(UserEntity.class, userId);
				this.logService.add("添加用户角色", "账号：[" + user.getAccount() + "]");
			} catch (Exception e) {
			}
		}
		return new Msg(true, "添加成功");
	}

	public Msg deleteRoleForUser(String roleId, String userId) {
		this.roleDao.executeSQL("delete from simple_user_roles where userId=? and roleId=?",
				new Object[] { userId, roleId });
		UserEntity user = this.userDao.load(UserEntity.class, userId);
		this.logService.add("删除用户角色", "账号：[" + user.getAccount() + "]");
		return new Msg(true, "删除成功");
	}
*/
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
	@Override
	public LoginUser loginCheck(LoginUser form) {
		// TODO Auto-generated method stub
		return null;
	}


}
