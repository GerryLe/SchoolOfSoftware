package com.rosense.basic.util.cons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 系统模块常用常量定义
 *
 */
public class Const {
	public final static String uploadDir = "attached";
	public final static String uploadFieldName = "Filedata";
	/**
	 * 用户登陆的Session Key
	 */
	public final static String USER_SESSION = "USER_SESSION";
	public final static String DEFAULTPASS = "123456";
	/**
	 * 主体principal
	 */
	public static final String PRINCIPAL_USER = "USER";
	public static final String PRINCIPAL_ROLE = "ROLE";
	public static final String PRINCIPAL_DEPT = "DEPT";
	public static final String PRINCIPAL_POSITION = "POSITION";
	public static final String PRINCIPAL_COMPANY = "COMPANY";
	public static Properties base = null;
	
	public static void loadBase(){
		try {
			base = loadPropertyFile("base.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Properties loadPropertyFile(String fullFile) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream resourceAsStream = loader.getResourceAsStream("config/" + fullFile);
			Properties p = System.getProperties();
			p.load(resourceAsStream);
			return p;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 后台管理主页根目录
	 */
	public static final String ADMIN_INDEX = "/WEB-INF/pages/";
	/**
	 * 系统管理根目录
	 */
	public static final String SYSTEM = "/WEB-INF/pages/system/";
	/**
	 * 公共页面根目录
	 */
	public static final String APP = "/WEB-INF/pages/app/";

}
