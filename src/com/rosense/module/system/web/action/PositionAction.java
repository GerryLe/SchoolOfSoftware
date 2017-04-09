package com.rosense.module.system.web.action;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.Msg;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IPositionService;
import com.rosense.module.system.web.form.PositionForm;

@Controller
@RequestMapping("/admin/system/position")
public class PositionAction extends BaseController {
	@Inject
	private IPositionService posService;

	@RequestMapping("/position_form_UI.do")
	public String position_form_UI(PositionForm form, Model model) throws Exception {
		if (StringUtil.isNotEmpty(form.getId())) {
			model.addAttribute("id", form.getId());
		}
		if (StringUtil.isNotEmpty(form.getPid())) {
			model.addAttribute("ttid", form.getPid());
			model.addAttribute("pid", form.getPid());
			form.setId(form.getPid());
			PositionForm form2 = posService.get(form);
			if (form2 != null) {
				model.addAttribute("pname", form.getName());
			} else {
				model.addAttribute("pname", "根目录");
			}
		} else {
			model.addAttribute("pname", "根目录");
		}
		return Const.SYSTEM + "position_form_UI";
	}

	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(PositionForm form) throws Exception {
		Msg msg = this.posService.add(form);
		this.posService.tree(null);
		return msg;
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(PositionForm form) throws Exception {
		Msg msg = this.posService.delete(form);
		this.posService.tree(null);
		return msg;
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(PositionForm form) throws Exception {
		Msg msg = this.posService.update(form);
		this.posService.tree(null);
		return msg;
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public PositionForm get(PositionForm form) throws Exception {
		return this.posService.get(form);
	}

	@RequestMapping("/tree.do")
	@ResponseBody
	public List<PositionForm> tree(PositionForm form) throws Exception {
		return this.posService.tree(form.getPid());
	}

}
