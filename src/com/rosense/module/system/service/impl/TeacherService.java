package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.net.URLEncoder;
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
import com.rosense.module.cache.Caches;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.ClassEntity;
import com.rosense.module.system.entity.PermitsMenuEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.TeacherEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.ITeacherService;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

import net.sf.json.JSONObject;

/**
 * 删除用户操作，只删除用户账号的数据，用户对于的员工信息见不会删除， 员工删除操作需员工管理页面下进行操作
 *
 */
@Service("teacherService")
@Transactional
public class TeacherService extends BaseService implements ITeacherService {
	private Logger logger = Logger.getLogger(TeacherService.class);
	@Inject
	private IBaseDao<UserEntity> userDao;
	@Inject
	private IBaseDao<TeacherEntity> teaDao;
	@Inject
	private IBaseDao<RoleEntity> roleDao;
	@Inject
	private IBaseDao<PermitsMenuEntity> permitsMenuDao;
	@Inject
	private IBaseDao<ClassEntity> basedaoClass;

	private FreemarkerUtil futil;

	@Autowired(required = true)
	public TeacherService(String ftlPath, String outPath) {
		if (null == futil) {
			futil = FreemarkerUtil.getInstance(ftlPath);
		}
	}

	public Msg addTea(UserForm form) {
		try {
			if (null == form.getTea_no() || "".equals(form.getTea_no()))
				return new Msg(false, "编号不能为空！");
			if (null == form.getEntrance_date_Str() || "".equals(form.getEntrance_date_Str()))
				return new Msg(false, "入职时间不能为空！");
			int equalsAccount = this.equlasVal("u.stu_no='" + form.getStu_no()+ "'");
			if (equalsAccount == 1)
				return new Msg(false, "该账号已存在！");
			if (form.getEntrance_date_Str() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				form.setEntrance_date(sdf.parse(form.getEntrance_date_Str()));

			}
			if(StringUtil.isEmpty(form.getGrade())){
				form.setGrade("教师");
			}
			final TeacherEntity tea = new TeacherEntity();
			BeanUtils.copyNotNullProperties(form, tea);
			this.teaDao.add(tea);
			final UserEntity u = new UserEntity();
			BeanUtils.copyNotNullProperties(form, u);
			u.setAccount(form.getTea_no());
			u.setPersonId(tea.getId());
			u.setName(form.getTea_name());
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
						.queryObject("select t from RoleEntity t where t.defaultRole=?", new Object[] { 3 });
				roles.add(defaultRole);
			}

			u.setRoles(roles);
			this.userDao.add(u);

			final Map<String, String> map = new HashMap<String, String>();
			map.put("ID", u.getId());
			map.put("PERSONID", tea.getId());
			map.put("NAME", StringUtil.toString(u.getName(), ""));
			map.put("PHOTO", StringUtil.toString(tea.getPhoto(), ""));
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
					//this.huDao.delete(HolidaysUsersEntity.class, user.getHolidaysId());
					this.teaDao.delete(TeacherEntity.class, user.getPersonId());
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
			u.setName(form.getTea_name());
			u.setTea_name(form.getStu_name());
			if (form.getEntrance_date_Str() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				form.setEntrance_date(sdf.parse(form.getEntrance_date_Str()));
			}
			TeacherEntity stu = this.teaDao.load(TeacherEntity.class, u.getPersonId());
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
						.queryObject("select t from RoleEntity t where t.defaultRole=?", new Object[] { 3 });
				roles.add(defaultRole);
			}

			u.setRoles(roles);
			this.teaDao.update(stu);
			this.userDao.update(u);
			this.logService.add("修改用户", "账号：[" + u.getAccount() + "]");
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改人员信息失败===>异常信息：", e);
			return new Msg(false, "修改人员信息失败！");
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
			logger.error("加载人员信息失败===>异常信息：", e);
			throw new ServiceException("加载用户信息异常：", e);
		}
	}

	// 获取信息
	public UserForm get(String id) {

		try {
			String sql = "select u.*,e.email,e.sex,e.phone,e.entrance_date_Str,e.province, e.grade,e.cornet,e.birthday,e.accountAddr,e.accountPro,e.idcard,e.nation,e.city,e.politicalFace,e.origin,e.contact,e.contactPhone,e.material,e.bankCard from simple_user u ";
			sql += " left join simple_teacher e ON(u.personId=e.id) ";
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
			String sql = "select u.*,e.email,e.sex,e.phone,e.entrance_date_Str,e.province, e.grade,e.cornet,e.birthday,e.accountAddr,e.accountPro,e.idcard,e.nation,e.city,e.politicalFace,e.origin,e.contact,e.contactPhone,e.material,e.bankCard from simple_user u ";
			sql += "right join simple_teacher e ON(e.id=u.personId)  ";
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
	 * 员工信息筛选
	 * @param sql
	 * @param form
	 * @param params
	 * @param selectType
	 * @param searchKeyName
	 * @return
	 */
	private String addWhereSearch(String sql, UserForm form, Map<String, Object> params,String selectType,String searchKeyName) {
				
				if (selectType.equals("tea_name")) {
					try {
						sql += " and e.tea_name like '%"+new String(searchKeyName.getBytes("ISO-8859-1"),"UTF-8")+"%'";
					} catch (Exception e) {
					}
				}
				if (selectType.equals("tea_no")) {
					try {
						sql += " and e.tea_no like '%"+searchKeyName+"%'";
					} catch (Exception e) {
					}
				}
				
		return sql;
	}
	
	private String addWhere(String sql, UserForm form, Map<String, Object> params) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if (key.equals("tea_name")) {
					form.setTea_name(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if (key.equals("tea_no")) {
					form.setTea_no(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				
			}
		}
		if (StringUtil.isNotEmpty(form.getTea_name())) {
			try {
				params.put("tea_name", "%%" + URLDecoder.decode(form.getTea_name(), "UTF-8") + "%%");
				sql += " and e.tea_name like :tea_name";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getTea_no())) {
			try {
				params.put("tea_no", "%%" + URLDecoder.decode(form.getTea_no(), "UTF-8") + "%%");
				sql += " and e.tea_no like :tea_no";
			} catch (Exception e) {
			}
		}

		return sql;
	}

	/*public Msg resetPwd(String id) {
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
*/
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
		String sql = "select u.*,e.class_id,e.email,e.sex,e.phone,e.entrance_date_Str,e.province, e.grade, e.birthday,e.accountAddr,e.accountPro,c.class_name class_name from simple_user u ";
		sql += "right join simple_teacher e ON(e.id=u.personId)  ";
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

	@Override
	public Msg importFile(List<UserForm> importUserList) {
		try {
			for (int i = 0; i < importUserList.size(); i++) {
				UserForm secUser = importUserList.get(i);
				int equalsAccount = this.equlasVal("u.account='" + secUser.getTea_no() + "'");
				if (equalsAccount == 1) {
					secUser.setId(this.getUserId(secUser.getTea_no()));
					this.update(secUser);
				} else {
					this.addTea(secUser);
				}
			}
			return new Msg(true, "导入信息成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入信息失败===>异常信息：", e);
			return new Msg(false, "导入信息失败！");
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
			String sql = "select u.*,e.email,e.sex,e.phone,e.entrance_date_Str,e.province, e.grade, e.birthday, e.accountAddr,e.accountPro from simple_user u ";
			sql += "left join simple_teacher e ON(e.id=u.personId)  ";
			sql += "where u.status=0 ";
			sql += "and u.account='" + WebContextUtil.getCurrentUser().getUser().getAccount() + "'";
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
	 * 获取当前用户信息
	 */
	/*public UserForm selectCurUser(){
		String sql="select e.*,c.class_name class_name from simple_user u left join simple_student e on(u.personId=e.id) ";
		sql+= "left join simple_class c on(c.id=e.class_id) ";
		sql+= "where u.id='"+WebContextUtil.getCurrentUser().getUser().getId()+"'";
		UserForm form=(UserForm) this.userDao.queryObjectSQL(sql,UserForm.class,false);
		if(form!=null){
			return form;
		}else{
			return null;
		}
	}*/
	
}
