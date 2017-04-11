package com.rosense.module.system.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.ImageUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.service.IClassService;
import com.rosense.module.system.service.IPositionService;
import com.rosense.module.system.service.IRoleService;
import com.rosense.module.system.service.ICourseService;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.PositionForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.ClassForm;
import com.rosense.module.system.web.form.CourseForm;

@Controller
@RequestMapping("/admin/system/course")
public class CourseAction extends BaseController {
	@Inject
	private ICourseService courseService;
    @Inject
    private IClassService classService;
	/**
	 * 添加课程
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(CourseForm form) throws Exception {
		return this.courseService.add(form);
	}

	/**
	 * 删除课程
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(CourseForm form) throws Exception {
		return this.courseService.delete(form);
	}

	
	/**
	 * 修改课程信息
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(CourseForm form) throws Exception {
		return this.courseService.update(form);
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public CourseForm get(CourseForm form) throws Exception {
		return this.courseService.get(form.getId());
	}


	/**
	 * 查询课程信息
	 */
	@RequestMapping("/datagridCourse.do")
	@ResponseBody
	public DataGrid datagridCourse(CourseForm form,String selectType,String searchKeyName) throws Exception {
		return this.courseService.datagridCourse(form,selectType,searchKeyName);
	}

	/**
	 * 获取班级信息
	 */
	@RequestMapping("datagridClass.do")
	@ResponseBody
	public DataGrid datagridClass(ClassForm form){
		return this.classService.datagridClass(form);
	}
	
} 

