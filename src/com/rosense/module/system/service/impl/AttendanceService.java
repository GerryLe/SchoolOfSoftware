package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
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
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.AttendanceEntity;
import com.rosense.module.system.service.IAttendanceService;
import com.rosense.module.system.web.form.AttendanceForm;
import com.rosense.module.system.web.form.UserForm;

import net.sf.json.JSONObject;

@Service("attendanceService")
@Transactional
public class AttendanceService implements IAttendanceService {

	private Logger logger = Logger.getLogger(AttendanceService.class);
	@Inject
	private IBaseDao<AttendanceEntity> attenDao;
	
	@Override
	public Msg add(AttendanceForm form) {
		try{
			//for(AttendanceForm form :forms){
			    form.setUid(form.getId());
				final AttendanceEntity stu = new AttendanceEntity();
				BeanUtils.copyProperties(form, stu);
				attenDao.add(stu);
				return new Msg(true, "操作成功！");
		     //}
			}catch(Throwable e){
			e.printStackTrace();
		}
		return new Msg(false, "操作失败！");
	}

	@Override
	public Msg update(AttendanceForm form) {
		//for(AttendanceForm form:forms){
			AttendanceEntity attendanceEntity=this.attenDao.load(AttendanceEntity.class, form.getId());
			form.setUid(attendanceEntity.getUid());
			BeanUtils.copyProperties(form, attendanceEntity);
			attenDao.update(attendanceEntity);	
		//}
		return new Msg(true, "操作成功！");
	}

	@Override
	public AttendanceForm get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGrid datagrid(AttendanceForm form) {
		if (null == form.getSort()) {
			SystemContext.setSort("u.account");
			SystemContext.setOrder("desc");
		}else {
			SystemContext.setSort("u." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		try{
			List<AttendanceForm> forms=new ArrayList<AttendanceForm>();
			Map<String, Object> alias=new HashMap<String,Object>();
			String sql="select u.id uuid, u.account stu_no,u.name stu_name ,a.* from simple_class c right join simple_student s on(c.id=s.class_id) ";
			sql += " left join simple_user u  on(u.personId=s.id) ";
			sql+=" left join simple_attendance a on(u.id=a.uid)";
			sql += "where u.status=0 ";
			 sql = addWhere(sql, form, alias);
			 Pager<AttendanceForm> pager = this.attenDao.findSQL(sql, alias, AttendanceForm.class, false);
				if (null != pager && !pager.getDataRows().isEmpty()) {
					for (AttendanceForm pf : pager.getDataRows()) {
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
	public DataGrid datagridPersonal(AttendanceForm form) {
		if (null == form.getSort()) {
			SystemContext.setSort("u.account");
			SystemContext.setOrder("desc");
		}else {
			SystemContext.setSort("u." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		try{
			List<AttendanceForm> forms=new ArrayList<AttendanceForm>();
			Map<String, Object> alias=new HashMap<String,Object>();
			String sql="select u.id uuid, u.account stu_no,u.name stu_name ,a.* from  simple_user u ";
			sql+=" right join simple_attendance a on(u.id=a.uid)";
			sql += "where u.id='"+WebContextUtil.getUserId()+"' ";
			 sql = addWhere(sql, form, alias);
			 Pager<AttendanceForm> pager = this.attenDao.findSQL(sql, alias, AttendanceForm.class, false);
				if (null != pager && !pager.getDataRows().isEmpty()) {
					for (AttendanceForm pf : pager.getDataRows()) {
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

	private String addWhere(String sql, AttendanceForm form, Map<String, Object> params) {
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
				if (key.equals("apply_date")) {
					form.setApply_date(StringUtil.getEncodePra(jsonObject.get(key).toString()));
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
		if (StringUtil.isNotEmpty(form.getApply_date())) {
			try {
				params.put("apply_date", "%%" + URLDecoder.decode(form.getApply_date(), "UTF-8") + "%%");
				sql += " and a.apply_date like :apply_date";
			} catch (Exception e) {
			}
		}

		return sql;
	}

}
