package com.rosense.module.system.web.action;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IResignationService;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.ResignationForm;

@Controller
@RequestMapping("/admin/system/resignation")
public class ResignationAction extends BaseController{
	@Inject
	private IResignationService resignationService ;
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(ResignationForm form){
		LoginSession loginSession = (LoginSession) session.getAttribute(Const.USER_SESSION);
		form.setUserId(loginSession.getUser().getUserId());
		form.setPersonId(loginSession.getUser().getPersonId());
		return this.resignationService.add(form);
	}
	
	@RequestMapping("/getImmediateBoss.do")
	@ResponseBody
	public List<ResignationForm> getImmediateBoss(String name){
		return this.resignationService.getImmediateBoss(name);
	}
	
	@RequestMapping("/addAudit.do")
	@ResponseBody
	public Msg addAudit(ResignationForm form){
		return this.resignationService.addAudit(form);
	}
	
	@RequestMapping("/getAudit.do")
	@ResponseBody
	public ResignationForm getAudit(ResignationForm form){
		return this.resignationService.getAudit(form);
	}
	
	@RequestMapping("/getcheck.do")
	@ResponseBody
	public boolean getcheck(String id){
		return this.resignationService.getcheck(id);
	}
	
	@RequestMapping("/getresignation.do")
	@ResponseBody
	public ResignationForm getresignation(){
		return this.resignationService.getresignation();
	}
	
	@RequestMapping("/datagridresignation.do")
	@ResponseBody
	public DataGrid datagridresignation(ResignationForm form){
		return this.resignationService.datagridresignation(form);
	}
	
	@RequestMapping("/getAuditById.do")
	@ResponseBody
	public ResignationForm getAuditById(String id){
		return this.resignationService.getAuditById(id);
	}
	
	@RequestMapping("/sendEmail.do")
	@ResponseBody
	public Msg sendEmail(String title,String content,String[] str){
		return this.resignationService.sendEmail(title,content,str);
	}
	
	@RequestMapping("/getexist.do")
	@ResponseBody
	public boolean getexist(){
		return this.resignationService.getexist();
	}
	
	@RequestMapping("/revocation.do")
	@ResponseBody
	public Msg revocation(String id){
		return this.resignationService.revocation(id);
	}
}
