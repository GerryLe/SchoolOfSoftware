package com.rosense.module.system.web.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.ITransferService;
import com.rosense.module.system.web.form.TransferForm;

@Controller
@RequestMapping("/admin/system/transfer")
public class TransferAction extends BaseController {

	
	@Resource
	private ITransferService transferService;
	
	@RequestMapping("add.do")
	@ResponseBody
	public Msg add(TransferForm form){
		return transferService.add(form);
	}
	
	@RequestMapping("select.do")
	@ResponseBody
	public DataGrid select(TransferForm form){
		return transferService.select(form);
	}
	
	@RequestMapping("get.do")
	@ResponseBody
	public TransferForm get(TransferForm form){
		return transferService.get(form.getId());
	}
	
	@RequestMapping("true.do")
	@ResponseBody
	public Msg approvaltrue(TransferForm form){
		return transferService.approvaltrue(form);
	}
	
	
	@RequestMapping("false.do")
	@ResponseBody
	public Msg approvalfalse(TransferForm form){
		return transferService.approvalfalse(form);
	}
	
	@RequestMapping("update.do")
	@ResponseBody
	public Msg update(TransferForm form){
		return transferService.update(form);
	}
	
	@RequestMapping("delete.do")
	@ResponseBody
	public Msg delete(TransferForm form){
		return transferService.delete(form);
	}
	@RequestMapping("notice.do")
	@ResponseBody
	public Msg notice(TransferForm form){
		return transferService.approvalnotice(form.getId());
	}
	
	@RequestMapping("getCurrentUserDefaultRole.do")
	@ResponseBody
	public int getCurrentUserDefaultRole(){
		return transferService.getCurrentUserDefaultRole();
	}
}


