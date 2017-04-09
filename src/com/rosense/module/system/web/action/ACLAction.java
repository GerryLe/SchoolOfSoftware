package com.rosense.module.system.web.action;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IACLService;
import com.rosense.module.system.service.IMenuService;
import com.rosense.module.system.web.form.ACLForm;
import com.rosense.module.system.web.form.MenuForm;

@Controller
@RequestMapping("/admin/system/acl")
public class ACLAction extends BaseController {
	@Inject
	private IACLService aclService;
	@Inject
	private IMenuService menuService;

	@RequestMapping("/no_grantPermits.do")
	public @ResponseBody
	Msg grantPermits(ACLForm form) {
		return this.aclService.grantPermits(form);
	}

	@RequestMapping("/getPermits.do")
	public @ResponseBody
	ACLForm getPermits(ACLForm form) throws Exception {
		return this.aclService.getPermits(form);
	}

	@RequestMapping("/no_allMenuTree.do")
	public @ResponseBody
	List<MenuForm> allMenuTree() throws Exception {
		return this.menuService.getAllMenuTree(null);
	}

}
