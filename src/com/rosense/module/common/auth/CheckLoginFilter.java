package com.rosense.module.common.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Can-Dao
 *  2014年12月18日 上午9:53:13
 * 
 */
public class CheckLoginFilter implements Filter {
	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = path.substring(contextPath.length());
		if (url.endsWith("/") || url.endsWith("login.jsp") || url.endsWith("noSession.jsp") || url.endsWith("noLogin.jsp") || url.endsWith("noSecurity.jsp") || url.endsWith("noLock.jsp")) {
			filterChain.doFilter(request, response);
			return;
		}
		filterChain.doFilter(request, response);
	}

	
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
