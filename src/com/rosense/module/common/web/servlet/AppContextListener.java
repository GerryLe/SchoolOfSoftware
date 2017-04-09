package com.rosense.module.common.web.servlet;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
	
	
	public void contextInitialized(ServletContextEvent evt) {
		//记录启动时间
		AppConst.START_DATE = new Date();
	}
	
	
	public void contextDestroyed(ServletContextEvent evt) {
		//清空结果
		AppConst.START_DATE = null;
		AppConst.MAX_ONLINE_COUNT_DATE = null;
	}
	

}
