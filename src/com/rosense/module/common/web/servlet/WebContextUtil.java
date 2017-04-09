package com.rosense.module.common.web.servlet;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;

import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.SystemPath;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.springmvc.ContextHolderUtils;
import com.rosense.module.system.service.IIncrementService;
import com.rosense.module.system.service.IUserService;
import com.rosense.module.system.web.form.AuthForm;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.UserForm;

public class WebContextUtil {
	public static Set<String> caches = new HashSet<String>();
	public static Hashtable<String, Long> addcaches = new Hashtable<String, Long>();
	private static WebApplicationContext wac;
	/**
	 * 上下文
	 */
	private static ServletContext sc;

	/**
	 * 上下文的绝对路径
	 * @param realPath
	 * @return
	 */
	public static String getRealPath(String realPath) {
		return getSc().getRealPath(realPath);
	}

	/**
	 * 项目的相对路径
	 * @return
	 */
	public static String getWebAppContextPath() {
		return getSc().getContextPath();
	}

	public static String getAttachedRootPath(String path) {
		return getRealPath(Const.uploadDir + SystemPath.getSeparator() + path) + File.separator;
	}

	public static String getAttachedPath(String path) {
		return File.separator + Const.uploadDir + SystemPath.getSeparator() + path + File.separator;
	}

	private WebContextUtil() {
	}

	public static void setWac(WebApplicationContext wac) {
		WebContextUtil.wac = wac;
	}

	public static WebApplicationContext getWac() {
		return wac;
	}

	public static Object getBean(String name) {
		return wac.getBean(name);
	}

	public static ServletContext getSc() {
		return sc;
	}

	public static void setSc(ServletContext sc) {
		WebContextUtil.sc = sc;
	}

	/**
	 * 获取当前登陆用户对象
	 * @return
	 */
	public static LoginSession getCurrentUser() {
		if (null != ContextHolderUtils.getSession()) {
			return (LoginSession) ContextHolderUtils.getSession().getAttribute(Const.USER_SESSION);
		} else {
			return null;
		}
	}

	/**
	 * 是否登录
	 */
	public static boolean isLogin() {
		return getCurrentUser().getUser() != null;
	}

	/**
	 * 获取用户Id
	 * @return
	 */
	public static String getUserId() {
		return getCurrentUser().getUser().getUserId();
	}
	
	/**
	 * 获取用户角色
	 * @return
	 */
	public static String getDefaultRole() {
		String defaultRole=String.valueOf(getCurrentUser().getUser().getDefaultRole());
		return defaultRole;
	}

	/**
	 * 获取用户名称
	 * @return
	 */
	public static String getUserName() {
		return getCurrentUser().getUser().getName();
	}

	/**
	 * 获取当前用户的权限
	 * @return
	 */
	public static AuthForm getCurrentUserAuth() {
		HttpSession session = ContextHolderUtils.getSession();
		if (null != session) {
			LoginSession ls = (LoginSession) session.getAttribute(Const.USER_SESSION);
			if (null != ls) {
				return ls.getAuth();
			}
		}
		return null;
	}

	private static IUserService userService;

	/**
	 * 根据id获取用户
	 */
	public static UserForm getUserForm(String id) {
		if (userService == null)
			userService = (IUserService) wac.getBean("userService");
		UserForm form = userService.get(id);
		if (form == null) {
			return new UserForm();
		}
		return form;
	}

	/**
	 * 是否管理员
	 */
	public static boolean isManager(String... name) {
		String roles = WebContextUtil.getCurrentUser().getUser().getRole_names();
		if (StringUtil.isNotEmpty(roles)) {
			roles = "," + roles + ",";
			for (String n : name) {
				if (roles.contains("," + n + ",")) {
					return true;
				}
			}
		}
		return false;
	}

	private static IIncrementService incrementService;

	public static synchronized int getNextId(String name) {
		if (incrementService == null)
			incrementService = (IIncrementService) wac.getBean("increment");
		return incrementService.nextId(name);
	}

}
