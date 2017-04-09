package com.rosense.module.system.web.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.ICircuitcardService;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/circuitcard")
public class CircuitcardAction extends BaseController {

	
	@Resource
	private ICircuitcardService circuitcardService;
	
	@RequestMapping("add.do")
	@ResponseBody
	public Msg add(UserForm form){
		return circuitcardService.add(form);
	}
	
	
	
	@RequestMapping("select.do")
	@ResponseBody
	public DataGrid select(UserForm form){
		return circuitcardService.select(form);
	}
	
	
	@RequestMapping("get.do")
	@ResponseBody
	public UserForm get(UserForm form){
		return circuitcardService.get(form.getId());
	}
	
	@RequestMapping("true.do")
	@ResponseBody
	public Msg approvaltrue(UserForm form){
		return circuitcardService.approvaltrue(form);
	}
	
	
	@RequestMapping("false.do")
	@ResponseBody
	public Msg approvalfalse(UserForm form){
		return circuitcardService.approvalfalse(form);
	}
	
	@RequestMapping("update.do")
	@ResponseBody
	public Msg update(UserForm form){
		return circuitcardService.update(form);
	}
	
	@RequestMapping("delete.do")
	@ResponseBody
	public Msg delete(UserForm form){
		return circuitcardService.delete(form);
	}
	@RequestMapping("notice.do")
	@ResponseBody
	public Msg notice(UserForm form){
		return circuitcardService.approvalnotice(form.getId());
	}
	
	@RequestMapping("complete.do")
	@ResponseBody
	public Msg complete(UserForm form){
		return circuitcardService.complete(form);
	}
	
	@RequestMapping("roleDefault.do")
	@ResponseBody
	public int roleDefault(){
		return circuitcardService.roleDefault();
	}
}


