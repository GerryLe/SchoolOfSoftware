package com.rosense.module.common.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rosense.basic.util.cons.Const;
import com.rosense.module.system.web.form.LoginSession;

/**
 * 首页
 * @author Can-Dao
 * 	
 * 2015年8月19日 上午11:14:15
 *
 */
@Controller
@RequestMapping("/admin/index")
public class IndexAction extends BaseController {

	/**
	 * 普通用户登录账号
	 */
	@RequestMapping("/index")
	public String index(HttpSession session, Model mode) {
		LoginSession user = (LoginSession) session.getAttribute(Const.USER_SESSION);
		if (null == user) {
			return "redirect:/common/errors/noLogin.jsp";
		}
		Map<String, String> map = new HashMap<String, String>();
		mode.addAttribute("cache", map);
		mode.addAttribute("userId", user.getUser().getUserId());
		mode.addAttribute("userName", user.getUser().getName());
		return Const.ADMIN_INDEX + "index";
	}

}
