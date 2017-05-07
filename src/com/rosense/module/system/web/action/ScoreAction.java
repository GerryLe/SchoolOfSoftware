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
import com.rosense.module.system.service.IScoreService;
import com.rosense.module.system.service.IOrgService;
import com.rosense.module.system.service.IPositionService;
import com.rosense.module.system.service.IRoleService;
import com.rosense.module.system.service.IUserService;
import com.rosense.module.system.web.form.ScoreForm;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.PositionForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/score")
public class ScoreAction extends BaseController {
	@Inject
	private IScoreService scoreService;
	
	
	/**
	 * 添加
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(ScoreForm form) throws Exception {
		return this.scoreService.add(form);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(UserForm form) throws Exception {
		return this.scoreService.delete(form);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(ScoreForm form) throws Exception {
		return this.scoreService.update(form);
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public ScoreForm get(ScoreForm form) throws Exception {
		return this.scoreService.get(form.getId());
	}

	/**
	 * 查询信息
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(ScoreForm form) throws Exception {
		return this.scoreService.datagrid(form);
	}
} 
