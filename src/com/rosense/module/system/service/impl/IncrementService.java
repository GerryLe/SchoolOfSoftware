package com.rosense.module.system.service.impl;

import javax.inject.Inject;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.module.system.entity.IncrementEntity;
import com.rosense.module.system.service.IIncrementService;


@Service("increment")
@Transactional
public class IncrementService implements IIncrementService {
	@Inject
	private IBaseDao<IncrementEntity> incrementDao;

	
	public int nextId(String id) {
		IncrementEntity entity = (IncrementEntity) this.incrementDao.queryObject("from IncrementEntity where id=?", id);
		if (entity == null) {
			entity = new IncrementEntity();
			entity.setValue(1);
			entity.setId(id);
			this.incrementDao.add(entity);
		} else {
			entity.setValue(entity.getValue() + 1);
			this.incrementDao.update(entity);
		}
		return entity.getValue();
	}

}
