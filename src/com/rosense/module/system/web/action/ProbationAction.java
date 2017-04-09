package com.rosense.module.system.web.action;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.ICompleteProbationService;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/probation")
public class ProbationAction extends BaseController {

	
	@Inject
	private ICompleteProbationService probationService;
	

	@RequestMapping("select.do")
	@ResponseBody
	public DataGrid select(UserForm form){
		return probationService.select(form);
	}
	
	@RequestMapping("get.do")
	@ResponseBody
	public UserForm get(UserForm form){
		return probationService.get(form.getId());
	}
	
	@RequestMapping("true.do")
	@ResponseBody
	public Msg approvaltrue(UserForm form){
		return probationService.approvaltrue(form);
	}
	
	
	@RequestMapping("false.do")
	@ResponseBody
	public Msg approvalfalse(UserForm form){
		return probationService.approvalfalse(form);
	}
	
	@RequestMapping("notice.do")
	@ResponseBody
	public Msg approvalnotice(String id){
		return probationService.approvalnotice(id);
	}
	
	
	@RequestMapping("getCurrentUserDefaultRole.do")
	@ResponseBody
	public int getCurrentUserDefaultRole(){
		return probationService.getCurrentUserDefaultRole();
	}
}


