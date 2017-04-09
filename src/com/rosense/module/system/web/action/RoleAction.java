package com.rosense.module.system.web.action;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IRoleService;
import com.rosense.module.system.web.form.RoleForm;

@Controller
@RequestMapping("/admin/system/role")
public class RoleAction extends BaseController {
	@Inject
	private IRoleService roleService;

	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(RoleForm form) throws Exception {
		return this.roleService.add(form);
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(RoleForm form) throws Exception {
		return this.roleService.delete(form);
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(RoleForm form) throws Exception {
		return this.roleService.update(form);
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public RoleForm get(RoleForm form) throws Exception {
		return this.roleService.get(form);
	}

	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(RoleForm form) throws Exception {
		return this.roleService.datagrid(form);
	}

}
