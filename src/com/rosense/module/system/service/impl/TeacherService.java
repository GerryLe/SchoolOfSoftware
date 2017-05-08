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
			
			/*
			 * if(!StringUtil.isEmpty(p.getCallerid())){ //座机分号
			 * p.setCallerid(p.getLocateid()+"-"+p.getCallerid()); }
			 */
			this.teaDao.update(stu);
			this.userDao.update(u);
			/*extenService.update(p.getLocateid(), p.getCallerid());*/
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

	/*public DataGrid datagrid_ref(UserForm form) {
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
			logger.error("加载人员列表信息失败===>异常信息：", e);
			throw new ServiceException("加载人员列表信息异常：", e);
		}
	}*/

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
					if (!StringUtil.isEmpty(pf.getEmploymentStr())) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
						String[] fromDate = pf.getEmploymentStr().split("/");
						String[] toDate = df.format(new Date()).split("/");
						pf.setWorkAge((Integer.parseInt(toDate[0]) - Integer.parseInt(fromDate[0])) + "年");
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

	/*@Override
	public DataGrid commondatagridperson(UserForm form,String selectType,String searchKeyName) {
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
		 //RoleEntity currole=this.roleDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
		 UserForm curform=(UserForm) this.userDao.queryObjectSQL("select e.* from simple_user u left join simple_person e on(u.personId=e.id) where u.id='"+WebContextUtil.getCurrentUser().getUser().getId()+"'",UserForm.class,false);
		try {
			List<UserForm> forms = new ArrayList<UserForm>();
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select u.*,e.locateid,e.callerid,e.chinaname,e.sex,e.employmentStr,e.email,e.phone,e.becomeStaffDate,e.job,e.province,e.leaveDate, e.birthday,e.securityDate,e.school,e.profession,e.graduation, e.degree,e.accountAddr,e.accountPro,e.address,e.age,e.workAge,e.probationLimit,e.probationEnd,e.marriage,e.agreementEndDate,e.agreementLimit,e.positionEng, e.agreementStartDate,e.agreementTimes,e.area,e.workOld,e.material,e.bankCard,e.bear,e.idcard,e.nation,e.origin,e.train,e.securityCard,e.politicalFace,e.certificate,e.contact,e.contactPhone,e.fund,e.fundDate,o.name orgName,oc.name orgChildName,p.name positionName from simple_user u ";
			sql += "left join simple_person e ON(e.id=u.personId)  ";
			sql += "left join simple_org o ON(e.orgId=o.id)  ";
			sql += "left join simple_org oc ON(e.orgChildId=oc.id)  ";
			sql += "left join simple_position p ON(e.positionId=p.id)  ";
			sql += "where u.status=0 ";
			if (roleId.contains(1)) {
				sql += "and u.name= '" + WebContextUtil.getCurrentUser().getUser().getName() + "'";
			}else if(roleId.contains(3)){
				sql+="";
			}else if(roleId.contains(2)||roleId.contains(4)){
				if(!StringUtil.isEmpty(curform.getOrgChildId())){
					sql += "  and e.orgChildId in('" + curform.getOrgChildId() + "'";
					sql = getOrgByPid(sql, curform.getOrgChildId());
					sql += ")";
				}
			}
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
					if (!StringUtil.isEmpty(pf.getEmploymentStr())) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
						String[] fromDate = pf.getEmploymentStr().split("/");
						String[] toDate = df.format(new Date()).split("/");
						pf.setWorkAge((Integer.parseInt(toDate[0]) - Integer.parseInt(fromDate[0])) + "年");
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

	// 查询离职用户信息
	public DataGrid datagridleaveperson(UserForm form) {
       
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
			
			 * String sql =
			 * "select u.*,e.chinaname,e.email, e.employmentDate, e.birthday, e.degree, e.sex, e.phone,e.province,e.city, e.genre, e.becomeStaffDate, e.job, e.employ, e.countStartDate,o.name orgName,p.name positionName from simple_user u "
			 * ;
			 
			String sql = "select u.*,e.chinaname,e.email,e.sex,e.employmentStr,e.phone,e.becomeStaffDate,e.province,e.job,e.leaveDate, e.birthday,e.securityDate,e.school,e.profession,e.graduation, e.degree,e.accountAddr,e.accountPro,e.address,e.age,e.workAge,e.probationLimit,e.probationEnd,e.marriage,e.agreementEndDate,e.agreementLimit,e.positionEng, e.agreementStartDate,e.agreementTimes,e.area,e.workOld,e.material,e.bankCard,e.bear,e.idcard,e.nation,e.origin,e.train,e.securityCard,e.politicalFace,e.certificate,e.contact,e.contactPhone,e.fund,e.fundDate,o.name orgName,oc.name orgChildName,p.name positionName from simple_user u ";
			sql += "left join simple_person e ON(e.id=u.personId)  ";
			sql += "left join simple_org o ON(e.orgId=o.id)  ";
			sql += "left join simple_org oc ON(e.orgChildId=oc.id)  ";
			sql += "left join simple_position p ON(e.positionId=p.id)  ";
			sql += "where u.status=0 and e.job='离职'  ";
			sql = addWhere(sql, form, alias);
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
					if (!StringUtil.isEmpty(pf.getEmploymentStr())) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
						String[] fromDate = pf.getEmploymentStr().split("/");
						String[] toDate = df.format(new Date()).split("/");
						pf.setWorkAge((Integer.parseInt(toDate[0]) - Integer.parseInt(fromDate[0])) + "年");

						// long to= df.parse(df.format(new Date())).getTime();
						// long from =
						// df.parse(pf.getEmploymentStr()).getTime();
						// System.out.println((to - from) / (1000 * 60 * 60 *
						// 24));
						// pf.setWorkAge(Long.toString(((to - from) / (1000 * 60
						// * 60 * 24)))+"天");
					} else {

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
			logger.error("加载人员列表信息失败===>异常信息：", e);
			throw new ServiceException("加载人员列表信息异常：", e);
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
	}*/

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
						//params.put("name", "%%" + URLDecoder.decode(searchKeyName, "UTF-8") + "%%");
						sql += " and e.tea_name like '%"+URLEncoder.encode(searchKeyName)+"%'";
					} catch (Exception e) {
					}
				}
				if (selectType.equals("tea_no")) {
					try {
						//params.put("chinaname", "%%" + URLDecoder.decode(searchKeyName, "UTF-8") + "%%");
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
	/*
	private int equlasValByPerson(String param) {
		String sql = "select p.* from simple_person p where " + param;
		return this.teaDao.countSQL(sql, false).intValue();
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
	}*/

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

		// 鑾峰彇瑙掕壊
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

	/**
	 * 获取登陆用户的权限 如果同时拥有用户授权，角色授权，部门授权，岗位授权，则进行权限累加，并去除重复的权限
	 */

	/*public AuthForm getAuth(String userId) {
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
	/*public Msg addOrgForUser(String orgId, String users) {
		String sql="select * from simple_org where id='"+orgId+"'";
		OrgForm orgForm=(OrgForm) basedaoClass.queryObjectSQL(sql,OrgForm.class,false);
		if(orgForm!=null){
			
			for (String userId : users.split(",")) {
				try {
					UserForm user = get(userId);
					userId = user.getPersonId();
					if(orgForm.getPid()!=null){
						this.roleDao.executeSQL("update simple_person set orgId=?, orgChildId=? where id=?", new Object[] {orgForm.getPid(), orgId, userId });
					}else{
						this.roleDao.executeSQL("update simple_person set orgId=?, orgChildId=? where id=?", new Object[] {orgId, orgId, userId });
					}
					this.logService.add("添加用户部门", "账号：[" + user.getAccount() + "]");
				} catch (Exception e) {
				}
			}
			return new Msg(true, "添加成功");
		}else{
			return new Msg(false,"添加失败");
		}
		
	}

	public Msg deleteOrgForUser(String userId) {
		this.roleDao.executeSQL("update simple_person set orgId=null where id=?", new Object[] { userId });
		final LoginUser user = WebContextUtil.getCurrentUser().getUser();
		this.logService.add("删除用户部门", "账号：[" + user.getAccount() + "]");
		return new Msg(true, "删除成功");
	}

	public Msg addPositionForUser(String positionId, String users) {
		for (String userId : users.split(",")) {
			try {
				UserForm user = get(userId);
				userId = user.getPersonId();
				this.roleDao.executeSQL("update simple_person set positionId=? where id=?",
						new Object[] { positionId, userId });
				this.logService.add("添加用户职位", "账号：[" + user.getAccount() + "]");
			} catch (Exception e) {
			}
		}
		return new Msg(true, "添加成功");
	}

	public Msg deletePositionForUser(String userId) {
		this.roleDao.executeSQL("update simple_person set positionId=null where id=?", new Object[] { userId });
		final LoginUser user = WebContextUtil.getCurrentUser().getUser();
		this.logService.add("删除用户职位", "账号：[" + user.getAccount() + "]");
		return new Msg(true, "删除成功");
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
			TeacherEntity entity = this.teaDao.load(TeacherEntity.class, user.getPersonId());
			entity.setPhoto(photo);
			this.teaDao.update(entity);
			user.setPhoto(photo);
			Map<String, String> map = Caches.getMap("USER", user.getUserId());
			map.put("PHOTO", photo);
			Caches.set("USER", user.getUserId(), map);
			this.logService.add("上传照片", "账号：[" + user.getAccount() + "]");
		} catch (Exception e) {
		}
		return new Msg(true);
	}*/

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

	/*@Override
	public List<UserForm> searchUsersData() {
		List<UserForm> forms = new ArrayList<UserForm>();
		try {
			Date date = new Date();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
			String toDate = sd.format(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sd.parse(toDate));
			cal.add(Calendar.DAY_OF_YEAR, +30);
			String nextDate = sd.format(cal.getTime());
			String sql = "select * from simple_person where becomeStaffDate between '" + toDate + "' and '" + nextDate
					+ "'";
			//System.out.println(sql);
			forms = this.userDao.listSQL(sql, UserForm.class, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return forms;
	}
*/


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
					if (!StringUtil.isEmpty(pf.getEmploymentStr())) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
						String[] fromDate = pf.getEmploymentStr().split("/");
						String[] toDate = df.format(new Date()).split("/");
						pf.setWorkAge((Integer.parseInt(toDate[0]) - Integer.parseInt(fromDate[0])) + "年");
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
