package com.rosense.module.common.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebServlet(loadOnStartup = 1, urlPatterns = "/initServlet")
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	public void init() throws ServletException {
		//获取spring的工厂
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		WebContextUtil.setWac(wac);
		//获取上下文
		WebContextUtil.setSc(this.getServletContext());
		//获取附件上传的根目录
		super.init();

	}

}
