package com.rosense.module.system.web.action;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IExtraworkapplysService;
import com.rosense.module.system.web.form.ExtraworkapplysForm;

@Controller
@RequestMapping("/admin/system/Extraworkapplys")
public class ExtraworkapplysAction extends BaseController {
	@Inject
	private IExtraworkapplysService extraworkapplysService;

	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(ExtraworkapplysForm form) throws Exception {
		return this.extraworkapplysService.add(form);
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(ExtraworkapplysForm form) throws Exception {
		return this.extraworkapplysService.delete(form);
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(ExtraworkapplysForm form) throws Exception {
		return this.extraworkapplysService.update(form);
	}

	@RequestMapping("/select.do")
	@ResponseBody
	public DataGrid get(ExtraworkapplysForm form) throws Exception {
		return this.extraworkapplysService.select(form);

	}

	@RequestMapping("/get.do")
	@ResponseBody
	public ExtraworkapplysForm get(String id) throws Exception {
		return this.extraworkapplysService.get(id);

	}
	
	@RequestMapping("/true.do")
	@ResponseBody
	public Msg approvaltrue(String id) throws Exception {
		return this.extraworkapplysService.approvaltrue(id);

	}
	
	@RequestMapping("/false.do")
	@ResponseBody
	public Msg approvalfalse(String id) throws Exception {
		return this.extraworkapplysService.approvalfalse(id);

	}

}
