package com.rosense.module.system.web.action;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IScoreService;
import com.rosense.module.system.web.form.ScoreForm;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/score")
public class ScoreAction extends BaseController {
	@Inject
	private IScoreService scoreService;
	
	
	/**
	 * 添加
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(ScoreForm form) throws Exception {
		return this.scoreService.add(form);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(UserForm form) throws Exception {
		return this.scoreService.delete(form);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(ScoreForm form) throws Exception {
		return this.scoreService.update(form);
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public ScoreForm get(ScoreForm form) throws Exception {
		return this.scoreService.get(form.getId());
	}

	/**
	 * 查询信息
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(ScoreForm form) throws Exception {
		return this.scoreService.datagrid(form);
	}
	
	/**
	 * 查询个人成绩信息
	 */
	@RequestMapping("/datagridPersonal.do")
	@ResponseBody
	public DataGrid datagridPersonal(ScoreForm form) throws Exception {
		return this.scoreService.datagridPersonal(form);
	}
} 
