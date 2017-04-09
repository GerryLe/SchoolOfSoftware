package com.rosense.module.system.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.SystemPath;
import com.rosense.module.system.entity.AttachEntity;
import com.rosense.module.system.service.IAttachService;

@Service
@Transactional
public class AttachService implements IAttachService {
	@Inject
	private IBaseDao<AttachEntity> attachDao;

	
	public boolean addAttach(AttachEntity fu) {
		try {
			if (StringUtil.isEmpty(fu.getId())) {
				fu.setCreated(new Date());
				attachDao.add(fu);
			} else {
				attachDao.update(fu);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	
	public boolean deleteAttach(String id) {
		AttachEntity fu = attachDao.load(AttachEntity.class, id);
		try {
			attachDao.delete(fu);
		} catch (Exception e) {
			return false;
		}
		File f = new File(SystemPath.getSysPath() + fu.getFilePath());
		if (f.exists()) {
			f.delete();
		}
		return true;
	}

	
	public List<AttachEntity> queryAttachs(String pid) {
		return attachDao.list(" from AttachEntity where pid=?", new Object[] { pid });
	}

	
	public AttachEntity get(String id) {
		return (AttachEntity) attachDao.getCurrentSession().get(AttachEntity.class, id);
	}

}
