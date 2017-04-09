package com.rosense.module.system.service;

import com.rosense.basic.model.Msg;
import com.rosense.module.system.entity.GlobalEntity;

public interface IGlobalService {

	/**
	 * 添加一个变量
	 */
	public Msg addOrUpdate(GlobalEntity global);

	/**
	 * 获取一个变量
	 */
	public GlobalEntity get(String gCode);

	void init(GlobalEntity entity);

}
