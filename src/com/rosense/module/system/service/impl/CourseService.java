package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
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
import com.rosense.basic.util.StringUtil;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.system.entity.CourseEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.service.ICourseService;
import com.rosense.module.system.web.form.CourseForm;

import net.sf.json.JSONObject;

/**
 * 删除用户操作，只删除用户账号的数据，用户对于的员工信息见不会删除， 员工删除操作需员工管理页面下进行操作
 *
 */
@Service("courseService")
@Transactional
public class CourseService extends BaseService implements ICourseService {
	private Logger logger = Logger.getLogger(CourseService.class);
	@Inject
	private IBaseDao<CourseEntity> courseDao;
	@Inject
	private IBaseDao<RoleEntity> roleDao;

	private FreemarkerUtil futil;

	@Autowired(required = true)
	public CourseService(String ftlPath, String outPath) {
		if (null == futil) {
			futil = FreemarkerUtil.getInstance(ftlPath);
		}
	}

	public Msg add(CourseForm form) {
		try {
			SimpleDateFormat pf=new SimpleDateFormat("yyyy/MM/dd");
			if (null == form.getCourse_no() || "".equals(form.getCourse_no()))
				return new Msg(false, "编号不能为空！");
			if (null == form.getCourse_name() || "".equals(form.getCourse_name()))
				return new Msg(false, "入学时间不能为空！");
			int equalsAccount = this.equlasVal("c.course_no='" + form.getCourse_no()+ "'");
			if (equalsAccount == 1)
				return new Msg(false, "该编号已存在！");
			final CourseEntity course = new CourseEntity();
			BeanUtils.copyNotNullProperties(form, course);
			course.setCreateTime(pf.format(new Date()));
			this.courseDao.add(course);
			this.logService.add("添加用户", "账号：[" + form.getCourse_no() + "]");
			return new Msg(true, "添加成功！");
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("添加课程信息失败===>异常信息：", e);
			return new Msg(false, "添加失败！");
		}
	}

	public Msg delete(CourseForm form) {
		try {
			if (null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",");
				for (String id : ids) {
						this.courseDao.delete(CourseEntity.class, id);
					this.logService.add("删除课程·", "：[" + form.getCourse_name() + "]");
				}
			}
			return new Msg(true, "删除成功！");
		} catch (Exception e) {
			logger.error("根据ID[" + form.getIds() + "]删除课程信息失败===>异常信息：", e);
			return new Msg(false, "删除失败！");
		}
	}

	// 更新信息
	public Msg update(CourseForm form) {
		try {
			SimpleDateFormat pf=new SimpleDateFormat("yyyy/MM/dd");
			CourseEntity course = this.courseDao.load(CourseEntity.class, form.getId());
			BeanUtils.copyNotNullProperties(form, course);
			course.setCreateTime(pf.format(new Date()));
			this.courseDao.update(course);
			this.logService.add("修改用户", "账号：[" + course.getCourse_name() + "]");
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改课程信息失败===>异常信息：", e);
			return new Msg(false, "修改课程信息失败！");
		}
	}

	

	// 获取信息
	public CourseForm get(String id) {

		try {
			String sql = "select c.* from simple_course c ";
			sql += " where c.id=?";
			CourseForm form = (CourseForm) this.courseDao.queryObjectSQL(sql, new Object[] { id }, CourseForm.class, false);
			return form;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载人员信息失败===>异常信息：", e);
			throw new ServiceException("加载用户信息异常：", e);
		}
	}

	
	// 查询信息
	public DataGrid datagridCourse(CourseForm form,String selectType,String searchKeyName) {
		try {
			if(form.getSort()!=null){
				SystemContext.setSort("c.course_no");
			     SystemContext.setOrder("desc");
			}else{
				SystemContext.setSort("c.createTime");
			     SystemContext.setOrder("desc");
			}
			List<CourseForm> forms = new ArrayList<CourseForm>();
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select c.* from simple_course c ";
		    sql += "where c.status=0 ";
			if(StringUtil.isNotEmpty(searchKeyName)){
				sql=addWhereSearch(sql, form, alias,selectType,searchKeyName);
			}else{
			   sql = addWhere(sql, form, alias);
			}
			Pager<CourseForm> pager = this.courseDao.findSQL(sql, alias, CourseForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (CourseForm pf : pager.getDataRows()) {
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
		return this.courseDao.countSQL(sql, false).intValue();
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
	private String addWhereSearch(String sql, CourseForm form, Map<String, Object> params,String selectType,String searchKeyName) {
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
	
	private String addWhere(String sql, CourseForm form, Map<String, Object> params) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if (key.equals("course_name")) {
					form.setCourse_name(jsonObject.get(key).toString());
				}
				if (key.equals("course_no")) {
					form.setCourse_no(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				
			}
		}
		if (StringUtil.isNotEmpty(form.getCourse_name())) {
			try {
				params.put("course_name", "%%" + URLDecoder.decode(form.getCourse_name(), "UTF-8") + "%%");
				sql += " and c.course_name like :course_name";
			} catch (Exception e) {
			}
		}
		if (StringUtil.isNotEmpty(form.getCourse_no())) {
			try {
				params.put("course_no", "%%" + URLDecoder.decode(form.getCourse_no(), "UTF-8") + "%%");
				sql += " and c.course_no like :course_no";
			} catch (Exception e) {
			}
		}

		return sql;
	}

	@Override
	public List<CourseForm> tree() {
		try{
			String sql="select c.id,c.course_name from simple_course c where 1=1";
			List<CourseForm> forms=this.courseDao.listSQL(sql, CourseForm.class,false);
			if(forms!=null&&forms.size()>0){
				return forms;
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
