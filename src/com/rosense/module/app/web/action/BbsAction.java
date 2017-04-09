package com.rosense.module.app.web.action;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.StringUtil;
import com.rosense.module.app.service.IBbsService;
import com.rosense.module.app.web.form.BbsForm;
import com.rosense.module.app.web.form.BbsItemForm;
import com.rosense.module.common.web.action.BaseController;

/**
 * 用户互动 
 * @author Can-Dao
 * 	
 * 2015年8月24日 下午3:10:02
 *
 */
@Controller
@RequestMapping("/app/bbs")
public class BbsAction extends BaseController {
	public static final String app = "/WEB-INF/pages/app/";
	@Inject
	IBbsService bbsService;

	@RequestMapping("/view/{id}.html")
	public String view(@PathVariable String id, Model model) throws IOException {
		BbsForm form = this.bbsService.getView(id);
		if (form == null) {
			return "redirect:/";
		}
		model.addAttribute("bbs", form);
		model.addAttribute("answer", request.getParameter("answer"));
		model.addAttribute("title", form.getTitle());
		return app + "bbsview";
	}

	@RequestMapping("/publish.html")
	public String publish(String id, Model model) {
		BbsForm form = new BbsForm();
		if (StringUtil.isNotEmpty(id)) {
			form = this.bbsService.get(id);
		}
		model.addAttribute("bbs", form);
		return app + "publish";
	}

	@RequestMapping("/bbsmgr.html")
	public String bbs() {
		return app + "bbsmgr";
	}

	/**
	 * 发表帖子
	 */
	@RequestMapping("no_add")
	@ResponseBody
	public Msg no_add(BbsForm form) {
		form.setSessionId(session.getId());
		return this.bbsService.add(form);
	}

	/**
	 * 获取帖子
	 */
	@RequestMapping("get")
	@ResponseBody
	public BbsForm getSpace(String id) {
		BbsForm form = this.bbsService.get(id);
		return form;
	}

	@RequestMapping(value = "no_zan")
	@ResponseBody
	public Msg zan(String id) {
		return this.bbsService.zan(id, session.getId());
	}

	@RequestMapping(value = "no_zanItem")
	@ResponseBody
	public Msg zanItem(String id) {
		return this.bbsService.zanItem(id, session.getId());
	}

	/**
	 * 删除帖子
	 */
	@RequestMapping(value = "no_delete")
	@ResponseBody
	public Msg no_delete(String id) {
		return this.bbsService.delete(id);
	}

	/**
	* 发表帖子回复
	*/
	@RequestMapping(value = "no_addItem")
	@ResponseBody
	public Msg addItem(BbsItemForm form) {
		form.setSessionId(session.getId());
		return this.bbsService.addItem(form);
	}

	/**
	 * 发表帖子回复
	 */
	@RequestMapping(value = "no_recommend")
	@ResponseBody
	public Msg recommend(String id, boolean recommend) {
		return this.bbsService.recommend(id, recommend);
	}

	/**
	 * 删除帖子回复
	 */
	@RequestMapping(value = "no_deleteItem")
	@ResponseBody
	public Msg deleteItem(String id) {
		return this.bbsService.deleteItem(id);
	}

	/**
	 * 获取帖子
	 */
	@RequestMapping(value = "datagrid")
	@ResponseBody
	public DataGrid datagrid(BbsForm form) {
		return this.bbsService.datagrid(form);
	}

	@RequestMapping(value = "datagridmanager")
	@ResponseBody
	public DataGrid datagridmanager(BbsForm form) {
		return this.bbsService.datagridmanager(form);
	}

	@RequestMapping(value = "datagridmy")
	@ResponseBody
	public DataGrid datagridmy(BbsForm form) {
		return this.bbsService.datagridmy(form);
	}

	/**
	 * 获取帖子
	 */
	@RequestMapping(value = "datagriditem")
	@ResponseBody
	public DataGrid datagriditem(BbsItemForm form) {
		return this.bbsService.datagrid(form);
	}

}
