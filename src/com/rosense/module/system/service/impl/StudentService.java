package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.FreemarkerUtil;
import com.rosense.basic.util.MD5Util;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.cache.Caches;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.ClassEntity;
import com.rosense.module.system.entity.PermitsMenuEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.StudentEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IStudentService;
import com.rosense.module.system.web.form.ACLForm;
import com.rosense.module.system.web.form.AuthForm;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

import net.sf.json.JSONObject;


@Service("studentService")
@Transactional
public class StudentService extends BaseService implements IStudentService {
	private Logger logger = Logger.getLogger(StudentService.class);
	@Inject
	private IBaseDao<UserEntity> userDao;
	@Inject
	private IBaseDao<StudentEntity> stuDao;
	@Inject
	private IBaseDao<RoleEntity> roleDao;
	@Inject
	private IBaseDao<PermitsMenuEntity> permitsMenuDao;
	@Inject
	private IBaseDao<ClassEntity> basedaoClass;

	private FreemarkerUtil futil;

	@Autowired(required = true)
	public StudentService(String ftlPath, String outPath) {
		if (null == futil) {
			futil = FreemarkerUtil.getInstance(ftlPath);
		}
	}

	public Msg addStu(UserForm form) {
		try {
			if (null == form.getStu_no() || "".equals(form.getStu_no()))
				return new Msg(false, "编号不能为空！");
			if (null == form.getEntrance_date_Str() || "".equals(form.getEntrance_date_Str()))
				return new Msg(false, "入学时间不能为空！");
			int equalsAccount = this.equlasVal("u.stu_no='" + form.getStu_no()+ "'");
			if (equalsAccount == 1)
				return new Msg(false, "该账号已存在！");
			if (form.getEntrance_date_Str() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				form.setEntrance_date(sdf.parse(form.getEntrance_date_Str()));

			}
			if(StringUtil.isEmpty(form.getGrade())){
				form.setGrade("学生");
			}
			final StudentEntity stu = new StudentEntity();
			BeanUtils.copyNotNullProperties(form, stu);
			this.stuDao.add(stu);
			final UserEntity u = new UserEntity();
			BeanUtils.copyNotNullProperties(form, u);
			u.setAccount(form.getStu_no());
			u.setPersonId(stu.getId());
			u.setName(form.getStu_name());
			u.setPassword(MD5Util.md5(Const.DEFAULTPASS));

			Set<RoleEntity> roles = new HashSet<RoleEntity>();
			// 获取默认角色
			if (null != form.getRole_ids() && !"".equals(form.getRole_ids())) {
				String[] ids = form.getRole_ids().split(",");
				for (String id : ids) {
					roles.add(this.roleDao.load(RoleEntity.class, id));
				}
			} else {
				RoleEntity defaultRole = (RoleEntity) this.roleDao
						.queryObject("select t from RoleEntity t where t.defaultRole=?", new Object[] { 4 });
				roles.add(defaultRole);
			}

			u.setRoles(roles);
			this.userDao.add(u);

			final Map<String, String> map = new HashMap<String, String>();
			map.put("ID", u.getId());
			map.put("PERSONID", stu.getId());
			map.put("NAME", StringUtil.toString(u.getName(), ""));
			map.put("PHOTO", StringUtil.toString(stu.getPhoto(), ""));
			Caches.set("USER", u.getId(), map);

			this.logService.add("添加用户", "账号：[" + form.getStu_no() + "]");
			return new Msg(true, "添加成功！");
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("添加用户信息失败===>异常信息：", e);
			return new Msg(false, "添加失败！");
		}
	}

	public Msg delete(UserForm form) {
		try {
			if (null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",");
				for (String id : ids) {
					UserEntity user = this.userDao.load(UserEntity.class, id);
					if ("admin".equals(user.getAccount())) {
						return new Msg(false, "删除失败,不能删除admin用户！");
					}
					this.stuDao.delete(StudentEntity.class, user.getPersonId());
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

	// 更新信息
	public Msg update(UserForm form) {
		try {
			UserEntity u = this.userDao.load(UserEntity.class, form.getId());
			u.setName(form.getStu_name());
			if (form.getEntrance_date_Str() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				form.setEntrance_date(sdf.parse(form.getEntrance_date_Str()));
			}
			StudentEntity stu = this.stuDao.load(StudentEntity.class, u.getPersonId());
			BeanUtils.copyNotNullProperties(form, stu, new String[] { "id", "photo", "remark"});
			if (StringUtil.isNotEmpty(form.getClass_id())) {
				stu.setClass_id(form.getClass_id());
			}
			Set<RoleEntity> roles = new HashSet<RoleEntity>();
			// 获取默认角色
			if (null != form.getRole_ids() && !"".equals(form.getRole_ids())) {
				String[] ids = form.getRole_ids().split(",");
				for (String id : ids) {
					roles.add(this.roleDao.load(RoleEntity.class, id));
				}
			} else {
				RoleEntity defaultRole = (RoleEntity) this.roleDao
						.queryObject("select t from RoleEntity t where t.defaultRole=?", new Object[] { 4 });
				roles.add(defaultRole);
			}
			u.setRoles(roles);
			this.stuDao.update(stu);
			this.userDao.update(u);
			this.logService.add("修改用户", "账号：[" + u.getAccount() + "]");
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改学生信息失败===>异常信息：", e);
			return new Msg(false, "修改学生信息失败！");
		}
	}

	public List<UserForm> searchUsers(String name) {
		try {
			name = URLDecoder.decode(name, "utf-8");
			String sql = "select u.* from simple_user u where u.name like '%" + name + "%' or u.account like '%" + name
					+ "%'";
			return this.userDao.listSQL(sql, UserForm.class, false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载信息失败===>异常信息：", e);
			throw new ServiceException("加载用户信息异常：", e);
		}
	}

	// 获取信息
	public UserForm get(String id) {

		try {
			String sql = "select u.*,e.idcard,e.nation,e.politicalFace,e.origin,e.cornet,e.city,e.class_id,e.email,e.sex,e.phone,e.entrance_date_Str,e.province, e.grade, e.birthday, e.graduate_school,e.profession,e.accountAddr,e.accountPro,e.contact,e.contactPhone,e.material,c.class_name class_name from simple_user u ";
			sql += "right join simple_student e ON(e.id=u.personId)  ";
			sql += "left join simple_class c ON(e.class_id=c.id)  ";
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
			logger.error("加载人员信息失败===>异常信息：", e);
			throw new ServiceException("加载用户信息异常：", e);
		}
	}

	public DataGrid datagrid_ref(UserForm form) {
		SystemContext.setSort("u.created");
		SystemContext.setOrder("desc");
		try {
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "";
			if ("role".equals(form.getParam1())) {
				sql = "select  u.* from simple_user u right join simple_student e ON(e.id=u.personId) left join simple_user_roles ur on u.id=ur.userId where ur.roleId=:roleId ";
				alias.put("roleId", form.getId());
			} else if ("class".equals(form.getParam1())) {
				sql = "select  u.account, u.name from simple_user u left join simple_student e ON(e.id=u.personId) left join simple_class c on e.class_id=c.id where e.class_id=:class_id ";
				alias.put("class_id", form.getId());
			} 
			if (form.getEntrance_date() != null) {
				form.setEntrance_date_Str(DateUtils.formatYYYYMMDD(form.getEntrance_date()));
			}
			Pager<UserForm> pager = this.userDao.findSQL(sql, alias, UserForm.class, false);
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载列表信息失败===>异常信息：", e);
			throw new ServiceException("加载列表信息异常：", e);
		}
	}

	// 查询信息
	public DataGrid datagridperson(UserForm form,String selectType,String searchKeyName) {
		if (null == form.getSort()) {
			SystemContext.setSort("u.created");
			SystemContext.setOrder("desc");
		} else {
			SystemContext.setSort("u." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		try {
			List<UserForm> forms = new ArrayList<UserForm>();
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select u.*,e.idcard,e.nation,e.politicalFace,e.origin,e.cornet,e.city,e.class_id,e.email,e.sex,e.phone,e.entrance_date_Str,e.province, e.grade, e.birthday, e.graduate_school,e.profession,e.accountAddr,e.accountPro,e.contact,e.contactPhone,e.material,c.class_name class_name from simple_user u ";
			sql += "right join simple_student e ON(e.id=u.personId)  ";
			sql += "left join simple_class c ON(e.class_id=c.id)  ";
			sql += "where u.status=0 ";
			if(StringUtil.isNotEmpty(searchKeyName)){
				sql=addWhereSearch(sql, form, alias,selectType,searchKeyName);
			}else{
			   sql = addWhere(sql, form, alias);
			}
			Pager<UserForm> pager = this.userDao.findSQL(sql, alias, UserForm.class, false);
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
			logger.error("加载人员列表信息失败===>异常信息：", e);
			throw new ServiceException("加载人员列表信息异常：", e);
		}
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
		        sql+= " and c.id like '%"+form.getClass_id()+"%'";
				if (selectType.equals("class_name")) {
					try {
						sql += " and c.class_name like '%"+new String(searchKeyName.getBytes("ISO-8859-1"),"UTF-8")+"%'";
					} catch (Exception e) {
					}
				}
				if (selectType.equals("stu_name")) {
					try {
						sql += " and e.stu_name like '%"+new String(searchKeyName.getBytes("ISO-8859-1"),"UTF-8")+"%'";
					} catch (Exception e) {
					}
				}
				if (selectType.equals("stu_no")) {
					try {
						sql += " and e.stu_no like '%"+searchKeyName+"%'";
					} catch (Exception e) {
					}
				}
				
		return sql;
	}
	
	private String addWhere(String sql, UserForm form, Map<String, Object> params) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if (key.equals("class_name")) {
					form.setClass_name(jsonObject.get(key).toString());
				}
				if (key.equals("stu_name")) {
					form.setStu_name(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if (key.equals("stu_no")) {
					form.setStu_no(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				
			}
		}
		if(StringUtil.isNotEmpty(form.getClass_id())){
			try {
				params.put("class_id", "%%" + URLDecoder.decode(form.getClass_id(), "UTF-8") + "%%");
				sql += " and c.id like :class_id";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getClass_name())) {
			try {
				params.put("class_name", "%%" + URLDecoder.decode(form.getClass_name(), "UTF-8") + "%%");
				sql += " and c.class_name like :class_name";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getStu_name())) {
			try {
				params.put("stu_name", "%%" + URLDecoder.decode(form.getStu_name(), "UTF-8") + "%%");
				sql += " and e.stu_name like :stu_name";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getStu_no())) {
			try {
				params.put("stu_no", "%%" + URLDecoder.decode(form.getStu_no(), "UTF-8") + "%%");
				sql += " and e.stu_no like :stu_no";
			} catch (Exception e) {
			}
		}

		return sql;
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

	

	public LoginUser loginCheck(LoginUser form) {
		String sql = "select u.*,e.class_id,e.email,e.sex,e.phone,e.entrance_date_Str,e.province,e.cornet, e.grade, e.birthday, e.graduate_school,e.profession,e.accountAddr,e.accountPro,c.class_name class_name from simple_user u ";
		sql += "right join simple_student e ON(e.id=u.personId)  ";
		sql += "left join simple_class c ON(e.class_id=c.id)  ";
		List<Object> params = new ArrayList<Object>();
		String pwd = (null == form.getPassword() || "".equals(form.getPassword().trim()) ? "" : form.getPassword());
		sql += " where u.account=? and u.password=?";
		params.add(form.getAccount().trim());
		params.add(MD5Util.md5(pwd));
		LoginUser lu = (LoginUser) this.userDao.queryObjectSQL(sql, params.toArray(), LoginUser.class, false);
		if (lu == null) {
			return null;
		}

		List<RoleForm> roles = this.roleDao.listSQL(
				"select r.id, r.name,r.defaultRole from simple_user_roles t LEFT JOIN simple_role r on(r.id=t.roleId) WHERE t.userId=?",
				new Object[] { lu.getUserId() }, RoleForm.class, false);
		if (null != roles) {
			StringBuffer s1 = new StringBuffer();
			StringBuffer s2 = new StringBuffer();
			for (RoleForm r : roles) {
				s1.append(r.getId() + ",");
				s2.append(r.getName() + ",");
				lu.setDefaultRole(r.getDefaultRole());
			}
			lu.setRole_ids((s1.length() > 0 ? s1.deleteCharAt(s1.length() - 1).toString() : ""));
			lu.setRole_names((s2.length() > 0 ? s2.deleteCharAt(s2.length() - 1).toString() : ""));
		}
		if (null != lu) {
			this.userDao.updateByHql("update UserEntity set lastLoginTime=? where id=?",
					new Object[] { System.currentTimeMillis(), lu.getUserId() });
		}
		return lu;
	}

	

	public AuthForm getAuth(String userId) {
		AuthForm auth = new AuthForm();
		List<Object> tree = new ArrayList<Object>();
		List<ACLForm> opers = new ArrayList<ACLForm>();
		List<String> authUrl = new ArrayList<String>();

		// 获取用户的角色ID
		List<Object[]> roleIds = this.roleDao
				.listSQL("select r.userId, r.roleId from simple_user_roles r where r.userId=?", userId);

		//********************************* 用户权限 **************************************//*
		// 获取用户权限
		List<ACLForm> aclUserMenus = getAclMenus(userId, Const.PRINCIPAL_USER);
		List<ACLForm> aclUserOpers = getAclOpers(userId, Const.PRINCIPAL_USER);
		initTreeAndOpers(tree, opers, authUrl, aclUserMenus, aclUserOpers);

		//********************************* 角色权限 **************************************//*
		// 获取用户角色权限
		for (Object[] o : roleIds) {
			List<ACLForm> aclRoleMenus = getAclMenus((String) o[1], Const.PRINCIPAL_ROLE);
			List<ACLForm> aclRoleOpers = getAclOpers((String) o[1], Const.PRINCIPAL_ROLE);
			initTreeAndOpers(tree, opers, authUrl, aclRoleMenus, aclRoleOpers);
		}
		
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


	@Override
	public Msg importFile(List<UserForm> importUserList) {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < importUserList.size(); i++) {
				UserForm secUser = importUserList.get(i);
				int equalsAccount = this.equlasVal("u.account='" + secUser.getStu_no() + "'");
				if (equalsAccount == 1) {
					secUser.setId(this.getUserId(secUser.getStu_no()));
					this.update(secUser);
				} else {
					this.addStu(secUser);
				}
			}
			return new Msg(true, "导入学生信息成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入学生信息失败===>异常信息：", e);
			return new Msg(false, "导入学生信息失败！");
		}
	}


	/**
	 * 判断用户账号是否存在
	 */
	public int equlasValAccount(String account) {
		String sql = "select u.* from simple_user u where u.account='" + account + "'";
		return this.userDao.countSQL(sql, false).intValue();
	}


	/**
	 * 查询用户个人资料
	 */
	@Override
	public DataGrid datagridpersonal() {
		// TODO Auto-generated method stub
		try {
			List<UserForm> forms = new ArrayList<UserForm>();
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select u.*,e.class_id,e.email,e.sex,e.phone,e.entrance_date_Str,e.province, e.cornet,e.grade, e.birthday, e.graduate_school,e.profession,e.accountAddr,e.accountPro,c.class_name class_name from simple_user u ";
			sql += "left join simple_student e ON(e.id=u.personId)  ";
			sql += "left join simple_class c ON(e.class_id=c.id)  ";
			sql += "where u.status=0 ";
			sql += "and u.id='" + WebContextUtil.getCurrentUser().getUser().getId() + "'";
			Pager<UserForm> pager = this.userDao.findSQL(sql, alias, UserForm.class, false);
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
			logger.error("加载人员列表信息失败===>异常信息：", e);
			throw new ServiceException("加载人员列表信息异常：", e);
		}

	}

}
