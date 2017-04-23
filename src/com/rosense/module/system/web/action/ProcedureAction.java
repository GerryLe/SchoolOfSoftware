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
import com.rosense.module.system.service.IProcedureService;
import com.rosense.module.system.web.form.OrgForm;

/**
 * 部门控制器
* @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Controller
@RequestMapping("/admin/system/procedure")
public class ProcedureAction extends BaseController {
	@Inject
	private IProcedureService procedureService;

	
	@RequestMapping("/treeToJSON.do")
	@ResponseBody
	public Object treeToJSON(OrgForm e) {
			//返回的是拼装的JSON字符串，需将字符串转换为JSON对象
		return JSON.parse(procedureService.treeToJSON(e.getPid()));
	}
	
}
