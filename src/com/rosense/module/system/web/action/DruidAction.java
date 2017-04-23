package com.rosense.module.system.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rosense.module.common.web.action.BaseController;

/**
 * druid
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Controller
@RequestMapping("/druid")
public class DruidAction extends BaseController {
	/**
	 * 转向到数据源监控页面
	 */
	@RequestMapping("/druid.do")
	public String druid() {
		return "redirect:/druid/index.html";
	}

}
