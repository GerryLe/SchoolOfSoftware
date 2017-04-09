package com.rosense.module.system.web.action;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.ILogService;
import com.rosense.module.system.web.form.LogForm;
import com.rosense.module.system.web.form.LoginLogForm;

@Controller
@RequestMapping("/admin/system/log")
public class LogAction extends BaseController {
	@Inject
	private ILogService logService;

	@RequestMapping("/delete.do")
	public @ResponseBody
	Msg delete(LogForm form) {
		return this.logService.delete(form);
	}

	@RequestMapping("/datagrid.do")
	public @ResponseBody
	DataGrid datagrid(LogForm form) {
		return this.logService.datagrid(form);
	}

	@RequestMapping("/deleteLL.do")
	public @ResponseBody
	Msg deleteLL(LoginLogForm form) {
		return this.logService.deleteLL(form);
	}

	@RequestMapping("/datagridLL.do")
	public @ResponseBody
	DataGrid datagridLL(LoginLogForm form) {
		return this.logService.datagridLL(form);
	}

}
