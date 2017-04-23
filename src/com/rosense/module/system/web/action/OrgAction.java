package com.rosense.module.system.web.action;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IOrgService;
import com.rosense.module.system.web.form.OrgForm;

/**
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Controller
@RequestMapping("/admin/system/org")
public class OrgAction extends BaseController {
	@Inject
	private IOrgService orgService;

	@RequestMapping("/org_form_UI.do")
	public String org_form_UI(OrgForm form, Model model) throws Exception {
		if (StringUtil.isNotEmpty(form.getId())) {
			model.addAttribute("id", form.getId());
		}
		if (StringUtil.isNotEmpty(form.getPid())) {
			model.addAttribute("pid", form.getPid());
			form.setId(form.getPid());
			OrgForm form2 = orgService.get(form);
			if (form2 != null) {
				model.addAttribute("pname", form.getName());
			} else {
				model.addAttribute("pname", "根目录");
			}
		} else {
			model.addAttribute("pname", "根目录");
		}
		return Const.SYSTEM + "org_form_UI";
	}

	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(OrgForm form) throws Exception {
		Msg msg = this.orgService.add(form);
		this.orgService.tree(null);
		return msg;
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(OrgForm form) throws Exception {
		Msg msg = this.orgService.delete(form);
		this.orgService.tree(null);
		return msg;
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(OrgForm form) throws Exception {
		Msg msg = this.orgService.update(form);
		return msg;
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public OrgForm get(OrgForm form) throws Exception {
		return this.orgService.get(form);
	}

	@RequestMapping("/tree.do")
	@ResponseBody
	public List<OrgForm> tree(OrgForm form) throws Exception {
		return this.orgService.tree(form.getPid());
	}
	
	
	@RequestMapping("/getUserCurrentAuthMenu.do")
	public @ResponseBody
	Object getUserCurrentAuthMenu(OrgForm e) {
			//返回的是拼装的JSON字符串，需将字符串转换为JSON对象
		String result="{" + "id : '" + e.getId() + "'" + ", name : '" + "can-dao" + "'" + ", iconCls : '" + e.getIconCls() + "'"
		+ ", sort : '" + e.getSort() +"'" + ", pid : '" + e.getPid() + "'" + ", children :[ " + orgService.userAndOrgtree(e.getPid()) + "]}";
		return JSON.parse(result);
	}
	
}
