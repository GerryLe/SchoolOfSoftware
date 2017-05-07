package com.rosense.module.common.init;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.util.MD5Util;
import com.rosense.module.system.entity.MenuEntity;
import com.rosense.module.system.entity.PermitsMenuEntity;
import com.rosense.module.system.entity.PermitsOperEntity;
import com.rosense.module.system.entity.TeacherEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.web.form.MenuForm;
/**
 * 
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Service
@Transactional
public class InitAdminService {
	@Autowired
	private IBaseDao<PermitsOperEntity> basedaoPermitsOper;
	@Autowired
	private IBaseDao<PermitsMenuEntity> basedaoPermitsMenu;
	@Autowired
	private IBaseDao<MenuEntity> basedaoMenu;
	@Autowired
	private IBaseDao<RoleEntity> basedaoRole;
	@Autowired
	private IBaseDao<TeacherEntity> teacherStudent;
	@Autowired
	private IBaseDao<UserEntity> basedaoUser;

	public void addInitAdmin() {
		//获取ROOT角色
		RoleEntity role = (RoleEntity) this.basedaoRole.queryObjectSQL("select t.* from simple_role t where t.sn=?", "ADMIN", RoleEntity.class,
				true);
		if (null == role)
			throw new ServiceException("找不到超级管理角色[ROOT]，无法完成角色授权和创建超级管理员账号！");

		// 删除所有的权限
		this.basedaoPermitsOper.executeSQL("delete from simple_permits_oper where principalId='" + role.getId() + "'");
		this.basedaoPermitsMenu.executeSQL("delete from simple_permits_menu where principalId='" + role.getId() + "'");
		//为ROOT角色授权
		getAllMenuTree(role.getId(), "ROLE");

		List<UserEntity> list = this.basedaoUser.list("from UserEntity where account='admin'");
		if (list == null || list.isEmpty()) {
			TeacherEntity p = new TeacherEntity();
			p.setSex("女");
			p.setEmail("1980921973@qq.com");
			p.setPhone("18320323579");
			p.setTea_name("admin");
			p.setTea_no("201314354117");
			this.teacherStudent.add(p);

			Set<RoleEntity> roles = new HashSet<RoleEntity>();
			roles.add(role);

			UserEntity u = new UserEntity();
			u.setAccount("admin");
			u.setName("admin");
			u.setTea_name("admin");
			u.setTea_no("201314354117");
			u.setPassword(MD5Util.md5("123456"));
			u.setStatus(0);
			u.setCreated(new Date());
			u.setRoles(roles);
			u.setPersonId(p.getId());
			this.basedaoUser.add(u);
		}
	}

	public List<MenuForm> getAllMenuTree(String principalId, String principalType) {
		String sql = "select t.* from simple_menu t where t.pid is null  and isShow=1  order by t.sort asc";
		List<MenuForm> list = this.basedaoMenu.listSQL(sql, MenuForm.class, false);

		List<MenuForm> forms = new ArrayList<MenuForm>();
		for (MenuForm e : list) {
			forms.add(allMenuNodes(principalId, principalType, e));
		}
		return forms;
	}

	/**
	 * @param principalId
	 * @param principalType
	 * @param form
	 * @return
	 */
	private MenuForm allMenuNodes(String principalId, String principalType, MenuForm form) {
		PermitsMenuEntity pmEntity = new PermitsMenuEntity();
		pmEntity.setMenuId(form.getId());
		pmEntity.setMenuHref(form.getHref());
		pmEntity.setMenuName(form.getName());
		pmEntity.setAlias(form.getAlias());
		pmEntity.setMenuPid(form.getPid());
		pmEntity.setMenuSort(form.getSort());
		pmEntity.setState(form.getState());
		pmEntity.setMenuIconCls(form.getIconCls());
		pmEntity.setMenuColor(form.getColor());
		pmEntity.setPrincipalId(principalId);
		pmEntity.setPrincipalType(principalType);
		this.basedaoPermitsMenu.add(pmEntity);

		List<MenuForm> menus = this.basedaoMenu.listSQL("select t.* from simple_menu t where t.pid='" + form.getId()
				+ "' and isShow=1 order by t.sort asc", MenuForm.class, false);
		if (null != menus && menus.size() > 0) {

			List<MenuForm> chlds = new ArrayList<MenuForm>();
			for (MenuForm e : menus) {
				if ("O".equals(e.getType())) {
					PermitsOperEntity poEntity = new PermitsOperEntity();
					poEntity.setPrincipalId(principalId);
					poEntity.setPrincipalType(principalType);
					poEntity.setOperMenuHref(e.getHref());
					poEntity.setOperMenuId(e.getId());
					poEntity.setOperMenuName(e.getName());
					poEntity.setOperIconCls(e.getIconCls());
					poEntity.setOperSort(e.getSort());
					poEntity.setState(e.getState());
					poEntity.setPermitsMenu(pmEntity);
					this.basedaoPermitsOper.add(poEntity);

					continue;
				}
				MenuForm recursive = this.allMenuNodes(principalId, principalType, e);
				chlds.add(recursive);
			}

			form.setChildren(chlds);
		}
		return form;
	}

}
