package com.rosense.module.system.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rosense.module.common.web.action.BaseController;

/**
 * druid
 * @author Can-Dao
 * 	
 * 2016年7月23日 下午8:54:51
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
