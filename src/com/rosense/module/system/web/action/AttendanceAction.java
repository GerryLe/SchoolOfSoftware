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
import com.rosense.module.system.service.IOrgService;
import com.rosense.module.system.service.IPositionService;
import com.rosense.module.system.service.IRoleService;
import com.rosense.module.system.service.IAttendanceService;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.PositionForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;
import com.rosense.module.system.web.form.AttendanceForm;

@Controller
@RequestMapping("/admin/system/attendance")
public class AttendanceAction extends BaseController {
	@Inject
	private IAttendanceService attenService;
	
	
	/**
	 * 添加考勤
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(List<AttendanceForm> forms) throws Exception {
		return this.attenService.add(forms);
	}

	/**
	 * 删除考勤
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(UserForm form) throws Exception {
		return this.attenService.delete(form);
	}

	/**
	 * 修改考勤
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(List<AttendanceForm> forms) throws Exception {
		return this.attenService.update(forms);
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public AttendanceForm get(AttendanceForm form) throws Exception {
		return this.attenService.get(form.getId());
	}

	/**
	 * 查询考勤信息
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(AttendanceForm form) throws Exception {
		return this.attenService.datagrid(form);
	}

} 
