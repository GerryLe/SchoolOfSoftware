package com.rosense.module.system.service.impl;

import java.awt.print.PrinterException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.log4j.Logger;
import org.jsoup.helper.DataUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.FreemarkerUtil;
import com.rosense.basic.util.MD5Util;
import com.rosense.basic.util.SendEmailUtil;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.cache.Caches;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.HolidaysUsersEntity;
import com.rosense.module.system.entity.ClassEntity;
import com.rosense.module.system.entity.PermitsMenuEntity;
import com.rosense.module.system.entity.AttendanceEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.TransferEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IAttendanceService;
import com.rosense.module.system.web.form.ACLForm;
import com.rosense.module.system.web.form.AuthForm;
import com.rosense.module.system.web.form.ClassForm;
import com.rosense.module.system.web.form.CourseForm;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.AttendanceForm;
import com.rosense.module.system.web.form.AttendanceForm;
import com.rosense.module.system.web.form.AttendanceForm;

import net.sf.json.JSONObject;

/**
 * 删除用户操作，只删除用户账号的数据，用户对于的员工信息见不会删除， 员工删除操作需员工管理页面下进行操作
 *
 */
@Service("attendanceService")
@Transactional
public class AttendanceService extends BaseService implements IAttendanceService {
	private Logger logger = Logger.getLogger(AttendanceService.class);
	@Inject
	private IBaseDao<AttendanceEntity> attendanceDao;
	@Inject
	private IBaseDao<RoleEntity> roleDao;

	private FreemarkerUtil futil;

	@Autowired(required = true)
	public AttendanceService(String ftlPath, String outPath) {
		if (null == futil) {
			futil = FreemarkerUtil.getInstance(ftlPath);
		}
	}

	public Msg add(List<AttendanceForm> listform) {
		try {
			SimpleDateFormat pf=new SimpleDateFormat("yyyy-MM-dd");
			String sql="select * form simple_attendance where apply_date='"+pf.format(new Date())+"'";
			List<AttendanceForm> forms=attendanceDao.listSQL(sql, AttendanceForm.class,false);
			//判断是否已经存在当天考勤信息
		    if(forms.size()>0&&forms!=null){
		    	update(listform);
		    	return new Msg(true, "提交成功！");
		    }
		    for(AttendanceForm form:listform){
		    	final AttendanceEntity attendance = new AttendanceEntity();
				BeanUtils.copyNotNullProperties(form, attendance);
				this.attendanceDao.add(attendance);
		    }
			return new Msg(true, "添加成功！");
		} catch (Throwable e) {
			e.printStackTrace();
			return new Msg(false, "添加失败！");
		}
	}

	public Msg delete(AttendanceForm form) {
		try {
			if (null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",");
				for (String id : ids) {
						this.attendanceDao.delete(AttendanceEntity.class, id);
				}
			}
			return new Msg(true, "删除成功！");
		} catch (Exception e) {
			return new Msg(false, "删除失败！");
		}
	}

	// 更新信息
	public Msg update(List<AttendanceForm> AttendanceForm) {
		try {
			SimpleDateFormat pf=new SimpleDateFormat("yyyy-MM-dd");
			for(AttendanceForm form:AttendanceForm){
				AttendanceEntity attendance = this.attendanceDao.load(AttendanceEntity.class, form.getId());
				BeanUtils.copyProperties(form, attendance);
				this.attendanceDao.update(attendance);
			}
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new Msg(false, "修改课程信息失败！");
		}
	}

	

	// 获取信息
	public AttendanceForm get(String id) {

		try {
			String sql = "select c.* from simple_course c ";
			sql += " where c.id=?";
			AttendanceForm form = (AttendanceForm) this.attendanceDao.queryObjectSQL(sql, new Object[] { id }, AttendanceForm.class, false);
			return form;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("加载用户信息异常：", e);
		}
	}

	
	// 查询信息
	public DataGrid datagridAttendance(AttendanceForm form,String selectType,String searchKeyName) {
		try {
			List<AttendanceForm> forms = new ArrayList<AttendanceForm>();
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select s.stu_name ,s.stu_no,a.*,c.class_name from simple_class c right join simple_student s on(s.class_id=c.id) left join simple_attendance a ";
		    sql += "where c.status=0 ";
			/*if(StringUtil.isNotEmpty(searchKeyName)){
				sql=addWhereSearch(sql, form, alias,selectType,searchKeyName);
			}else{
			   sql = addWhere(sql, form, alias);
			}*/
		    sql = addWhere(sql, form, alias);
			Pager<AttendanceForm> pager = this.attendanceDao.findSQL(sql, alias, AttendanceForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (AttendanceForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载人员列表信息失败===>异常信息：", e);
			throw new ServiceException("加载人员列表信息异常：", e);
		}
	}

	private int equlasVal(String param) {
		String sql = "select c.* from simple_course c where " + param;
		return this.attendanceDao.countSQL(sql, false).intValue();
	}


	/**
	 * 员工信息筛选
	 * @param sql
	 * @param form
	 * @param params
	 * @param selectType
	 * @param searchKeyName
	 * @return
	 */
	private String addWhereSearch(String sql, AttendanceForm form, Map<String, Object> params,String selectType,String searchKeyName) {
				if (selectType.equals("course_name")) {
					try {
						sql += " and c.course_name like '%"+searchKeyName+"%'";
					} catch (Exception e) {
					}
				}
				if (selectType.equals("course_no")) {
					try {
						sql += " and c.course_no like '%"+searchKeyName+"%'";
					} catch (Exception e) {
					}
				}
				
		return sql;
	}
	
	private String addWhere(String sql, AttendanceForm form, Map<String, Object> params) {
		
		if (StringUtil.isNotEmpty(form.getSchool_year())) {
			try {
				params.put("school_year", "%%" + URLDecoder.decode(form.getSchool_year(), "UTF-8") + "%%");
				sql += " and c.school_year like :school_year";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getSemester())) {
			try {
				params.put("semester", "%%" + URLDecoder.decode(form.getSemester(), "UTF-8") + "%%");
				sql += " and c.semester like :semester";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getClass_id())) {
			try {
				params.put("class_id", "%%" + URLDecoder.decode(form.getClass_id(), "UTF-8") + "%%");
				sql += " and c.class_id like :class_id";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getApply_date())) {
			try {
				params.put("apply_date", "%%" + URLDecoder.decode(form.getApply_date(), "UTF-8") + "%%");
				sql += " and c.apply_date like :apply_date";
			} catch (Exception e) {
			}
		}
		return sql;
	}


}
