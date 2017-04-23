package com.rosense.module.system.service.impl;

import java.util.List;

import javax.inject.Inject;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.StringUtil;
import com.rosense.module.system.entity.GlobalEntity;
import com.rosense.module.system.service.IGlobalService;

/**
 * 授权
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Service("globalService")
@Transactional
public class GlobalService implements IGlobalService {

	@Inject
	private IBaseDao<GlobalEntity> globalDao;

	
	public Msg addOrUpdate(GlobalEntity global) {
		if (StringUtil.isNotEmpty(global.getId())) {
			this.globalDao.update(global);
		} else {
			this.globalDao.add(global);
		}
		return new Msg();
	}

	
	public GlobalEntity get(String gCode) {
		List<GlobalEntity> List = this.globalDao.list("from GlobalEntity where gCode=?", new Object[] { gCode });
		if (List != null && List.size() > 0) {
			return List.get(0);
		}
		return null;
	}

	
	public void init(GlobalEntity entity) {
		GlobalEntity oldEntity = get(entity.getgCode());
		if (oldEntity == null) {
			this.globalDao.add(entity);
		} else {
			oldEntity.setgValue(entity.getgValue());
			this.globalDao.update(oldEntity);
		}
	}
}
