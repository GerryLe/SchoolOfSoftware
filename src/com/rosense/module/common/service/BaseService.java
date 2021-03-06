package com.rosense.module.common.service;

import javax.inject.Inject;

import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.service.ILogService;
import com.rosense.module.system.web.form.LoginUser;

/**
 * 
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public class BaseService {
	@Inject
	public ILogService logService;
	protected LoginUser currentUser;

	public LoginUser getCurrentUser() {
		if (null != WebContextUtil.getCurrentUser()) {
			return WebContextUtil.getCurrentUser().getUser();
		} else {
			return null;
		}
	}

	public void setCurrentUser(LoginUser currentUser) {
		this.currentUser = currentUser;
	}

}
