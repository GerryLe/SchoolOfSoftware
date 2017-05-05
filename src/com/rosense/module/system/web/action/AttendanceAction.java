package com.rosense.module.system.web.action;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IAttendanceService;
import com.rosense.module.system.web.form.AttendanceForm;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/attendance")
public class AttendanceAction extends BaseController {
	@Inject
	private IAttendanceService attenService;
	
	
	/**
	 * 添加考勤
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(List<AttendanceForm> forms) throws Exception {
		return this.attenService.add(forms);
	}

	/**
	 * 删除考勤
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(UserForm form) throws Exception {
		return this.attenService.delete(form);
	}

	/**
	 * 修改考勤
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(List<AttendanceForm> forms) throws Exception {
		return this.attenService.update(forms);
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public AttendanceForm get(AttendanceForm form) throws Exception {
		return this.attenService.get(form.getId());
	}

	/**
	 * 查询考勤信息
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(AttendanceForm form) throws Exception {
		return this.attenService.datagrid(form);
	}

} 
