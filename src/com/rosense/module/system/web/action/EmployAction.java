package com.rosense.module.system.web.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.impl.EmployService;
import com.rosense.module.system.web.form.EmployForm;

@Controller
@RequestMapping("/admin/system/employ")
public class EmployAction extends BaseController {

	
	@Resource
	private EmployService employService;
	
	
	@RequestMapping("add.do")
	@ResponseBody
	public Msg add(EmployForm form){
		return employService.add(form);
	}
	
	@RequestMapping("get.do")
	@ResponseBody
	public EmployForm get(String id){
		return employService.get(id);
	}
	
	@RequestMapping("update.do")
	@ResponseBody
	public Msg update(EmployForm form){
		return employService.update(form);
	}
	
	@RequestMapping("delete.do")
	@ResponseBody
	public Msg delete(EmployForm form){
		return employService.delete(form);
	}
	
	@RequestMapping("select.do")
	@ResponseBody
	public DataGrid select(EmployForm form){
		return employService.select(form);
	}
	
	
	@RequestMapping("true.do")
	@ResponseBody
	public Msg approvaltrue(String id){
		return employService.approvaltrue(id);
	}
	
	
	@RequestMapping("false.do")
	@ResponseBody
	public Msg approvalfalse(String id){
		return employService.approvalfalse(id);
	}
}


