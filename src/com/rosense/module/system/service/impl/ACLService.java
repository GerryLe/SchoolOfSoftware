package com.rosense.module.system.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.entity.MenuEntity;
import com.rosense.module.system.entity.PermitsMenuEntity;
import com.rosense.module.system.entity.PermitsOperEntity;
import com.rosense.module.system.service.IACLService;
import com.rosense.module.system.service.IMenuService;
import com.rosense.module.system.web.form.ACLForm;
import com.rosense.module.system.web.form.MenuForm;

/**
 * 授权
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Service
@Transactional
public class ACLService implements IACLService {
	@Inject
	private IBaseDao<PermitsMenuEntity> basedaoPermitsMenu;
	@Inject
	private IBaseDao<PermitsOperEntity> basedaoPermitsOper;
	@Inject
	private IMenuService menuService;

	
	public Msg grantPermits(ACLForm form) {
		//菜单
		List<MenuForm> menuList = menuService.getAllMenuTree(new MenuForm());
		Map<String, MenuForm> menuMap = new HashMap<String, MenuForm>();
		for (MenuForm a : menuList) {
			menuMap.put(a.getId(), a);
		}
		// 获取主体
		List<ACLForm> principals = JSON.parseArray(form.getPrincipals(), ACLForm.class);
		for (ACLForm p : principals) {
			// 删除原有的权限
			this.basedaoPermitsOper.executeSQL("delete from simple_permits_oper where principalId='" + p.getPrincipalId() + "' and principalType='" + p.getPrincipalType() + "'");
			this.basedaoPermitsMenu.executeSQL("delete from simple_permits_menu where principalId='" + p.getPrincipalId() + "' and principalType='" + p.getPrincipalType() + "'");

			// 获取许可菜单资源
			List<ACLForm> resources = JSON.parseArray(form.getResources(), ACLForm.class);
			PermitsMenuEntity pmEntity_temp = null;
			final Set<String> hasRes = new HashSet<String>();
			final Set<String> hasRes1 = new HashSet<String>();
			for (ACLForm r : resources) {
				try {
					MenuEntity menuEntity = menuService.getById(r.getMenuId());
					if (menuEntity != null) {
						if (!"O".equals(menuEntity.getType())) {
							hasRes.add(r.getMenuId());
							PermitsMenuEntity pmEntity = new PermitsMenuEntity();
							pmEntity.setMenuColor(menuEntity.getColor());
							pmEntity.setMenuHref(menuEntity.getHref());
							pmEntity.setMenuIconCls(menuEntity.getIconCls());
							pmEntity.setMenuId(menuEntity.getId());
							pmEntity.setMenuName(menuEntity.getName());
							pmEntity.setAlias(menuEntity.getAlias());
							MenuEntity menu1 = menuEntity.getMenu();
							if (menu1 != null)
								pmEntity.setMenuPid(menu1.getId());
							pmEntity.setMenuSort(menuEntity.getSort());
							pmEntity.setPrincipalId(p.getPrincipalId());
							pmEntity.setPrincipalType(p.getPrincipalType());
							// 保存许可菜单资源
							this.basedaoPermitsMenu.add(pmEntity);
							pmEntity_temp = pmEntity;
						} else {
							PermitsOperEntity poEntity = new PermitsOperEntity();
							poEntity.setOperIconCls(menuEntity.getIconCls());
							poEntity.setOperMenuHref(menuEntity.getHref());
							poEntity.setOperMenuId(menuEntity.getId());
							poEntity.setOperMenuName(menuEntity.getName());
							poEntity.setPrincipalId(p.getPrincipalId());
							poEntity.setPrincipalType(p.getPrincipalType());
							poEntity.setPermitsMenu(pmEntity_temp);

							// 保存许可的操作资源
							this.basedaoPermitsOper.add(poEntity);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (String res : hasRes) {
				MenuEntity menuEntity = menuService.getById(res);
				MenuEntity menu = menuEntity.getMenu();
				while (menu != null) {
					if (!hasRes.contains(menu.getId()) && !hasRes1.contains(menu.getId())) {
						hasRes1.add(menu.getId());
					}
					menu = menu.getMenu();
				}
			}
			for (String res : hasRes1) {
				try {
					MenuEntity menuEntity = menuService.getById(res);
					if (menuEntity != null) {
						PermitsMenuEntity pmEntity = new PermitsMenuEntity();
						pmEntity.setMenuColor(menuEntity.getColor());
						pmEntity.setMenuHref(menuEntity.getHref());
						pmEntity.setMenuIconCls(menuEntity.getIconCls());
						pmEntity.setMenuId(menuEntity.getId());
						pmEntity.setMenuName(menuEntity.getName());
						pmEntity.setAlias(menuEntity.getAlias());
						pmEntity.setSelectOnly(1);
						MenuEntity menu1 = menuEntity.getMenu();
						if (menu1 != null)
							pmEntity.setMenuPid(menu1.getId());
						pmEntity.setMenuSort(menuEntity.getSort());
						pmEntity.setPrincipalId(p.getPrincipalId());
						pmEntity.setPrincipalType(p.getPrincipalType());
						// 保存许可菜单资源
						this.basedaoPermitsMenu.add(pmEntity);
						pmEntity_temp = pmEntity;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return new Msg(true, "授权成功！");
	}

	
	public ACLForm getPermits(ACLForm form) {
		ACLForm aclPermits = new ACLForm();

		String sqlMenus = "select t.menuId from simple_permits_menu t where t.principalId=? and t.principalType=? and selectOnly=0";
		List<ACLForm> menuList = this.basedaoPermitsMenu.listSQL(sqlMenus, new Object[] { form.getPrincipalId(), form.getPrincipalType() }, ACLForm.class, false);

		String sqlOpers = "select t.operMenuId from simple_permits_oper t where t.principalId=? and t.principalType=?";
		List<ACLForm> operList = this.basedaoPermitsOper.listSQL(sqlOpers, new Object[] { form.getPrincipalId(), form.getPrincipalType() }, ACLForm.class, false);

		aclPermits.setMenus(menuList);
		aclPermits.setOpers(operList);

		return aclPermits;
	}

}
