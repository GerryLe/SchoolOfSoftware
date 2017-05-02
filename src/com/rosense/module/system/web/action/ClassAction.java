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
import com.rosense.module.system.service.IClassService;
import com.rosense.module.system.web.form.ClassForm;

/**
 * 班级控制层
 * @author 黄家乐
 * 	
 * 2017年3月20日 下午4:53:26
 *
 */
@Controller
@RequestMapping("/admin/system/class")
public class ClassAction extends BaseController {
	@Inject
	private IClassService classService;

	@RequestMapping("/class_form_UI.do")
	public String class_form_UI(ClassForm form, Model model) throws Exception {
		if (StringUtil.isNotEmpty(form.getId())) {
			model.addAttribute("id", form.getId());
		}
		if (StringUtil.isNotEmpty(form.getPid())) {
			model.addAttribute("pid", form.getPid());
			form.setId(form.getPid());
			ClassForm form2 = classService.get(form);
			if (form2 != null) {
				model.addAttribute("pname", form.getClass_name());
			} else {
				model.addAttribute("pname", "根目录");
			}
		} else {
			model.addAttribute("pname", "根目录");
		}
		return Const.SYSTEM + "class_form_UI";
	}

	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(ClassForm form) throws Exception {
		Msg msg = this.classService.add(form);
		this.classService.tree(null);
		return msg;
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(ClassForm form) throws Exception {
		Msg msg = this.classService.delete(form);
		this.classService.tree(null);
		return msg;
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(ClassForm form) throws Exception {
		Msg msg = this.classService.update(form);
		return msg;
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public ClassForm get(ClassForm form) throws Exception {
		return this.classService.get(form);
	}

	@RequestMapping("/pidtree.do")
	@ResponseBody
	public List<ClassForm> pidtree(ClassForm form) throws Exception {
		return this.classService.pidtree(form.getPid());
	}
	
	@RequestMapping("/tree.do")
	@ResponseBody
	public List<ClassForm> tree(ClassForm form) throws Exception {
		return this.classService.tree(form.getPid());
	}
	
	
	@RequestMapping("/getUserCurrentAuthMenu.do")
	public @ResponseBody
	Object getUserCurrentAuthMenu(ClassForm e) {
			//返回的是拼装的JSON字符串，需将字符串转换为JSON对象
		String result="{" + "id : '" + e.getId() + "'" + ", name : '" + "can-dao" + "'" + ", iconCls : '" + e.getIconCls() + "'"
		+ ", sort : '" + e.getSort() +"'" + ", pid : '" + e.getPid() + "'" + ", children :[ " + classService.userAndOrgtree(e.getPid()) + "]}";
		return JSON.parse(result);
	}
	
}
