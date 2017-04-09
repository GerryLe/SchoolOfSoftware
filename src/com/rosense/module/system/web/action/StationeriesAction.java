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
import com.rosense.module.system.entity.StationeriesEntity;
import com.rosense.module.system.entity.StationeryEntity;
import com.rosense.module.system.service.IStationeriesService;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.StationeryForm;

@Controller
@RequestMapping("/admin/system/stationeries")
public class StationeriesAction extends BaseController {
	@Inject
	private IStationeriesService stationeriesService ;
	
	@RequestMapping("/addstationery.do")
	@ResponseBody
	public Msg addstationery(StationeryForm form){
		LoginSession loginSession = (LoginSession) session.getAttribute(Const.USER_SESSION);
		LoginUser user = loginSession.getUser();
		form.setUserId(user.getId());
		form.setOrgId(user.getOrgId());
		return stationeriesService.addstationery(form);
	}
	
	@RequestMapping("/deletestationery.do")
	@ResponseBody
	public Msg deletestationery(StationeryForm form){
		return stationeriesService.deletestationery(form);
	}
	
	@RequestMapping("/updatestationery.do")
	@ResponseBody
	public Msg updatestationery(StationeryForm form){
		return stationeriesService.updatestationery(form);
	}
	
	@RequestMapping("/datagridstationery.do")
	@ResponseBody
	public DataGrid datagridstationery(StationeryForm form){
		LoginSession loginSession = (LoginSession) session.getAttribute(Const.USER_SESSION);
		LoginUser user = loginSession.getUser();
		form.setUserId(user.getUserId());
		return stationeriesService.datagridstationery(form);
	}
	
	@RequestMapping("/datagridstationeries.do")
	@ResponseBody
	public DataGrid datagridstationeries(StationeryForm form){
		return stationeriesService.datagridstationeries(form);
	}
	
	@RequestMapping("/addstationeries.do")
	@ResponseBody
	public Msg addstationeries(StationeryForm form){
		return stationeriesService.addstationeries(form);
	}
	
	@RequestMapping("/deletestationeries.do")
	@ResponseBody
	public Msg deletestationeries(StationeryForm form){
		return stationeriesService.deletestationeries(form);
	}
	
	@RequestMapping("/updatestationeries.do")
	@ResponseBody
	public Msg updatestationeries(StationeryForm form){
		return stationeriesService.updatestationeries(form);
	}
	
	@RequestMapping("/getstationeries.do")
	@ResponseBody
	public StationeriesEntity getstationeries(StationeryForm form){
		return stationeriesService.getstationeries(form);
	}
	
	@RequestMapping("/getstationerylist.do")
	@ResponseBody
	public List<StationeriesEntity> getstationerylist(){
		return stationeriesService.getstationerylist();
	}
	
	@RequestMapping("/getstationery.do")
	@ResponseBody
	public StationeryEntity getstationery(StationeryForm form){
		return stationeriesService.getstationery(form);
	}
	
	@RequestMapping("/datagridbuy.do")
	@ResponseBody
	public DataGrid datagridbuy(StationeryForm form){
		return stationeriesService.datagridbuy(form);
	}
	
	@RequestMapping("/tree.do")
	@ResponseBody
	public List<StationeryEntity> tree(){
		return stationeriesService.tree();
	}
	
	@RequestMapping("/bulkImport.do")
	@ResponseBody
	public Msg bulkImport(){
		return stationeriesService.bulkImport(request);
	}
}