package com.rosense.module.system.service;

import java.util.List;

import com.rosense.module.system.entity.AttachEntity;

/**
 * 附件管理
 * @author Can-Dao
 * 	
 * 2015年8月24日 下午3:32:03
 *
 */
public interface IAttachService {
	/**
	 * 删除附件
	 */
	public boolean deleteAttach(String id);

	/**
	 * 根据引用ID，获取相应的附件
	 * @param pid
	 * @return
	 */
	public List<AttachEntity> queryAttachs(String pid);

	/**
	 * 删除附件
	 */
	boolean addAttach(AttachEntity attachEntity);

	/**
	 * 获取单个附件
	 */
	public AttachEntity get(String id);
}
