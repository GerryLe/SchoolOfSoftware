package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.system.entity.ScoreEntity;
import com.rosense.module.system.service.IScoreService;
import com.rosense.module.system.web.form.ScoreForm;
import com.rosense.module.system.web.form.UserForm;

import net.sf.json.JSONObject;

@Service("scoreService")
@Transactional
public class ScoreService implements IScoreService {

	private Logger logger = Logger.getLogger(ScoreService.class);
	@Inject
	private IBaseDao<ScoreEntity> scoreDao;
	
	@Override
	public Msg add(ScoreForm form) {
		try{
		    form.setUid(form.getId());
		    form.setApply_date(DateUtils.formatYYYYMMDD(new Date()));
			final ScoreEntity stu = new ScoreEntity();
			BeanUtils.copyProperties(form, stu);
			scoreDao.add(stu);
			return new Msg(true, "操作成功！");
			}catch(Throwable e){
			e.printStackTrace();
		}
		return new Msg(false, "操作失败！");
	}

	@Override
	public Msg update(ScoreForm form) {
			ScoreEntity attendanceEntity=this.scoreDao.load(ScoreEntity.class, form.getId());
			form.setApply_date(DateUtils.formatYYYYMMDD(new Date()));
			form.setUid(attendanceEntity.getUid());
			BeanUtils.copyProperties(form, attendanceEntity);
			scoreDao.update(attendanceEntity);	
		return new Msg(true, "操作成功！");
	}

	@Override
	public ScoreForm get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGrid datagrid(ScoreForm form) {
		if (null == form.getSort()) {
			SystemContext.setSort("u.account");
			SystemContext.setOrder("desc");
		}else {
			SystemContext.setSort("u." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		try{
			List<ScoreForm> forms=new ArrayList<ScoreForm>();
			Map<String, Object> alias=new HashMap<String,Object>();
			String sql="select u.id uuid, u.account stu_no,u.name stu_name ,a.* from simple_class c right join simple_student s on(c.id=s.class_id) ";
			sql += " left join simple_user u  on(u.personId=s.id) ";
			sql+=" left join simple_score a on(u.id=a.uid) ";
			sql += "where u.status=0 ";
			 sql = addWhere(sql, form, alias);
			 Pager<ScoreForm> pager = this.scoreDao.findSQL(sql, alias, ScoreForm.class, false);
				if (null != pager && !pager.getDataRows().isEmpty()) {
					for (ScoreForm pf : pager.getDataRows()) {
						 forms.add(pf);
					}
				}
				DataGrid dg = new DataGrid();
				dg.setTotal(pager.getTotal());
				dg.setRows(forms);
				return dg;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("加载列表信息失败===>异常信息：", e);
			throw new ServiceException("加载列表信息异常：", e);
		}
	}

	@Override
	public Msg delete(UserForm form) {
		// TODO Auto-generated method stub
		return null;
	}

	private String addWhere(String sql, ScoreForm form, Map<String, Object> params) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if (key.equals("school_year")) {
					form.setSchool_year(jsonObject.get(key).toString());
				}
				if (key.equals("semester")) {
					form.setSemester(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if (key.equals("class_id")) {
					form.setClass_id(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if (key.equals("course_id")) {
					form.setCourse_id(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if (key.equals("stu_no")) {
					form.setStu_no(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if (key.equals("stu_name")) {
					form.setStu_name(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
			}
		}
		if(StringUtil.isNotEmpty(form.getSchool_year())){
			try {
				params.put("school_year", "%%" + URLDecoder.decode(form.getSchool_year(), "UTF-8") + "%%");
				sql += " and a.school_year like :school_year";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getSemester())) {
			try {
				params.put("semester", "%%" + URLDecoder.decode(form.getSemester(), "UTF-8") + "%%");
				sql += " and a.semester like :semester";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getClass_id())) {
			try {
				params.put("class_id", "%%" + URLDecoder.decode(form.getClass_id(), "UTF-8") + "%%");
				sql += " and c.id like :class_id";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getCourse_id())) {
			try {
				params.put("course_id", "%%" + URLDecoder.decode(form.getCourse_id(), "UTF-8") + "%%");
				sql += " and a.course_id like :course_id";
			} catch (Exception e) {
			}
		}

		if (StringUtil.isNotEmpty(form.getStu_no())) {
			try {
				params.put("stu_no", "%%" + URLDecoder.decode(form.getStu_no(), "UTF-8") + "%%");
				sql += " and u.account like :stu_no";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getStu_name())) {
			try {
				params.put("stu_name", "%%" + URLDecoder.decode(form.getStu_name(), "UTF-8") + "%%");
				sql += " and u.name like :stu_name";
			} catch (Exception e) {
			}
		}
		return sql;
	}

}
