/**
 * 
 */
package com.rosense.module.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.BeanUtils;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.PositionEntity;
import com.rosense.module.system.service.IPositionService;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.PositionForm;

/**
 *
 */
@Service("positionService")
@Transactional
public class PositionService extends BaseService implements IPositionService {
	@Inject
	private IBaseDao<PositionEntity> positionDao;

	
	public Msg add(PositionForm form) {
		PositionEntity position = new PositionEntity();
		BeanUtils.copyNotNullProperties(form, position);
		position.setSort(WebContextUtil.getNextId("positoin"));
		if (form.getPid() != null && !"".equalsIgnoreCase(form.getPid())) {
			position.setPosition(this.positionDao.load(PositionEntity.class, form.getPid()));
		}
		this.positionDao.add(position);
		this.logService.add("添加职位", "名称：[" + position.getName() + "]");
		return new Msg(true, "添加成功！");
	}

	
	public Msg delete(PositionForm form) {
		PositionEntity entity = this.positionDao.load(PositionEntity.class, form.getId());
		del(entity);
		return new Msg(true, "删除成功！");
	}

	private void del(PositionEntity position) {
		if (position.getPositions() != null && position.getPositions().size() > 0) {
			for (PositionEntity e : position.getPositions()) {
				del(e);
			}
		}
		this.positionDao.delete(PositionEntity.class, position.getId());
		this.positionDao.executeSQL("update simple_person set positionId=? where positionId=?", new Object[] { null, position.getId() });
		this.logService.add("删除职位", "名称：[" + position.getName() + "]");
	}

	
	public Msg update(PositionForm form) {
		PositionEntity position = this.positionDao.load(PositionEntity.class, form.getId());
		BeanUtils.copyNotNullProperties(form, position);

		if (null != form.getPid() && !"".equals(form.getPid())) {
			if (!position.getId().equals(form.getPid())) {
				position.setPosition(this.positionDao.load(PositionEntity.class, form.getPid()));
			} else {
				return new Msg(false, "操作有误，父模块服务关联自己！");
			}
		}
		this.positionDao.update(position);
		this.logService.add("修改职位", "名称：[" + position.getName() + "]");
		return new Msg(true, "修改成功！");
	}

	
	public PositionForm get(PositionForm form) {
		PositionEntity entity = this.positionDao.load(PositionEntity.class, form.getId());
		BeanUtils.copyNotNullProperties(entity, form);
		if (null != entity.getPosition()) {
			form.setPid(entity.getPosition().getId());
		}
		return form;
	}

	
	public List<PositionForm> tree(String pid) {
		String sql = "select t.* from simple_position t where t.pid is null order by t.sort asc";
		if (null != pid && !"".equals(pid.trim()))
			sql = "select t.* from simple_position t where t.id='" + pid + "'";

		List<PositionForm> list = this.positionDao.listSQL(sql, PositionForm.class, false);
		List<PositionForm> forms = new ArrayList<PositionForm>();
		for (PositionForm e : list) {
			forms.add(recursive(e));
		}
		return forms;
	}

	public PositionForm recursive(PositionForm form) {
		form.setText(form.getName());
		List<PositionForm> orgs = this.positionDao.listSQL("select t.* from simple_position t where t.pid='" + form.getId() + "' order by t.sort asc", PositionForm.class, false);
		if (null != orgs && orgs.size() > 0) {
			List<PositionForm> chlds = new ArrayList<PositionForm>();
			for (PositionForm e : orgs) {
				PositionForm recursive = this.recursive(e);

				PositionForm childform = new PositionForm();
				BeanUtils.copyNotNullProperties(recursive, childform);
				chlds.add(childform);
			}

			form.setChildren(chlds);
		}

		return form;
	}

	public PositionForm getId(String positionName) {
		String sql = "select t.* from simple_position t where t.name='" + positionName + "'";
		List<PositionForm> list = this.positionDao.listSQL(sql, PositionForm.class, false);
		PositionForm pf=new PositionForm();
		if (null != list && list.size() > 0) {
			for (PositionForm e : list) {
				 pf=e;
			}
		}
		return pf;
	}
}
