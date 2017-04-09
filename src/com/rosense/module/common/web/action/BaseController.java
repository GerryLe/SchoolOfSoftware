package com.rosense.module.common.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.rosense.basic.util.JsonUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.springmvc.DateEditor;
import com.rosense.module.system.service.ILogService;

/**
 * @类说明：基础控制器 其他控制器继承此控制器获得日期字段类型转换和防止XSS攻击的功能
 */
@Controller
@RequestMapping("/base")
public class BaseController {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	@Inject
	protected ILogService logService;

	/**
	 * 用户跳转JSP页面 此方法不考虑权限控制
	 */
	@RequestMapping("/{name}")
	public String base(@PathVariable String name, String id, Model model) {
		if (StringUtil.isNotEmpty(id)) {
			model.addAttribute("id", id);
		}
		model.addAttribute("url", getPath() + name + ".jsp");
		return Const.ADMIN_INDEX + "/template";
	}

	public String getPath() {
		return Const.SYSTEM;
	}

	/**
	 * 每次执行请求前都会先执行它再执行请求
	 */
	@ModelAttribute
	public void setServletApi(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
		model.addAttribute("webRoot", request.getContextPath());
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public ILogService getLogService() {
		return logService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new DateEditor());

		/**
		 * 防止XSS攻击
		 */
		// binder.registerCustomEditor(String.class, new
		// StringEscapeEditor(true, false));
	}

	/**
	 * 用户跳转JSP页面 此方法不考虑权限控制
	 */
	@RequestMapping("/{folder}/{jspName}")
	public String redirectJsp(@PathVariable String folder, @PathVariable String jspName) {
		return "/" + folder + "/" + jspName;
	}

	/**
	 * 输出JSON格式
	 */
	public void toJson(Object data, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.append(JsonUtils.object2json(data));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}

	}
	

}
