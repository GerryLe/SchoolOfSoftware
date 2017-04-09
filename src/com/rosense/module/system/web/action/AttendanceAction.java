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
import com.rosense.module.system.service.IUserService;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.PositionForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/attendance")
public class AttendanceAction extends BaseController {
	@Inject
	private IUserService userService;
	@Inject
	private IOrgService orgService;
	@Inject
	private IPositionService posService;
	@Inject
	private IRoleService roleService;
	
	
	/**
	 * 添加用户
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(UserForm form) throws Exception {
		return this.userService.addUser(form);
	}

	/**
	 * 删除用户
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(UserForm form) throws Exception {
		return this.userService.delete(form);
	}

	/**
	* 锁定账号
	*/
	@RequestMapping("/lockUser.do")
	@ResponseBody
	public Msg lockUser(String id) {
		return this.userService.lockUser(id);
	}

	/**
	 * 重置密码
	 */
	@RequestMapping("/resetPwd.do")
	@ResponseBody
	public Msg resetPwd(String id) {
		return this.userService.resetPwd(id);
	}

	/**
	 * 修改账号
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(UserForm form) throws Exception {
		return this.userService.update(form);
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public UserForm get(UserForm form) throws Exception {
		return this.userService.get(form.getId());
	}

	/**
	 * 查询账号
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(UserForm form,String selectType,String searchKeyName) throws Exception {
		return this.userService.datagrid(form,selectType,searchKeyName);
	}

	/**
	 * 查询用户信息
	 */
	@RequestMapping("/datagridperson.do")
	@ResponseBody
	public DataGrid datagridperson(UserForm form,String selectType,String searchKeyName) throws Exception {
		return this.userService.datagridperson(form,selectType,searchKeyName);
	}
	
	@RequestMapping("/commondatagridperson.do")
	@ResponseBody
	public DataGrid commondatagridperson(UserForm form,String selectType,String searchKeyName) throws Exception {
		return this.userService.commondatagridperson(form,selectType,searchKeyName);
	}
	
	/**
	 * 查询用户信息
	 */
	@RequestMapping("/datagridleaveperson.do")
	@ResponseBody
	public DataGrid datagridleaveperson(UserForm form) throws Exception {
		return this.userService.datagridleaveperson(form);
	}

	/**
	 * 关联查询一些部门，角色，职位相关 的用户
	 */
	@RequestMapping("/datagrid_ref.do")
	@ResponseBody
	public DataGrid datagrid_ref(UserForm form) throws Exception {
		if (StringUtil.isEmpty(form.getId())) {
			form.setId(StringUtil.toString(getSession().getAttribute("ref_id"), ""));
		} else {
			getSession().setAttribute("ref_id", form.getId());
		}
		return this.userService.datagrid_ref(form);
	}

	/**
	 * 查询用户
	 */
	@RequestMapping("/search.do")
	@ResponseBody
	public List<UserForm> search(String content) throws Exception {
		return this.userService.searchUsers(content);
	}

	/**
	 * 修改头像
	 */
	@RequestMapping("/updatephoto.do")
	@ResponseBody
	public Msg updatephoto() {
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String userId = WebContextUtil.getUserId();
			try {
				String savePath = WebContextUtil.getAttachedRootPath("photo");
				new File(savePath).mkdirs();
				String path = WebContextUtil.getAttachedPath("photo");
				List<MultipartFile> files = multipartRequest.getFiles(Const.uploadFieldName);
				if (files == null || files.size() < 1) {
					throw new RuntimeException("没有发生上传的文件！请检查");
				}
				if (files.size() > 0) {
					MultipartFile multipartFile = files.get(0);
					String orginalName = multipartFile.getOriginalFilename();
					String extension = FilenameUtils.getExtension(orginalName);
					String newName = userId + "." + extension;
					ImageUtils.thumbnail(multipartFile.getInputStream(), 100, 100, new FileOutputStream(savePath + newName));
					Msg msg = this.userService.updatePhoto(path + newName);
					msg.setObj(path + newName + "?t=" + System.currentTimeMillis());
					return msg;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Msg(false, "照片上传异常");
	}

	/**
	 * 修改密码
	 */
	@RequestMapping("/updatepwd.do")
	@ResponseBody
	public Msg updatepwd(UserForm form) {
		return this.userService.updatePwd(form);
	}

	/**
	 * 修改角色
	 */
	@RequestMapping("/updateRole.do")
	@ResponseBody
	public Msg batchRole(UserForm form) {
		return this.userService.batchUserRole(form);
	}

	/**
	 * 添加角色用户
	 */
	@RequestMapping("/adduserrole.do")
	@ResponseBody
	public Msg no_adduserrole(String roleId, String userIds) throws Exception {
		return this.userService.addRoleForUser(roleId, userIds);
	}

	/**
	 * 删除角色用户
	 */
	@RequestMapping("/deleteuserrole.do")
	@ResponseBody
	public Msg deleteuserrole(String roleId, String userId) throws Exception {
		return this.userService.deleteRoleForUser(roleId, userId);
	}

	/**
	 * 添加部门用户
	 */
	@RequestMapping("/adduserorg.do")
	@ResponseBody
	public Msg adduserorg(String orgId, String userIds) throws Exception {
		return this.userService.addOrgForUser(orgId, userIds);
	}

	/**
	 * 删除部门用户
	 */
	@RequestMapping("/deleteuserorg.do")
	@ResponseBody
	public Msg deleteuserorg(String userId) throws Exception {
		return this.userService.deleteOrgForUser(userId);
	}

	/**
	 * 添加职位用户
	 */
	@RequestMapping("/adduserposition.do")
	@ResponseBody
	public Msg adduserposition(String positionId, String userIds) throws Exception {
		return this.userService.addPositionForUser(positionId, userIds);
	}

	/**
	 * 删除职位用户
	 */
	@RequestMapping("/deleteuserposition.do")
	@ResponseBody
	public Msg deleteuserposition(String userId) throws Exception {
		return this.userService.deletePositionForUser(userId);
	}
	
	
	
	/**
	 * 判断导入用户信息是否重复
	 */
	@RequestMapping("/checkUserAccount.do")
	@ResponseBody
	public List<UserForm> importUserCheck(@RequestParam(value="uploadFile")MultipartFile mFile, HttpServletRequest request, HttpServletResponse response) {
		String rootPath = request.getSession().getServletContext().getRealPath(File.separator);
		List<UserForm> secUserList = new ArrayList<UserForm>();
			String userId = WebContextUtil.getUserId();
			try {
					String orginalName = mFile.getOriginalFilename();
					 String filePath = "uploadFileCheck/" +userId + orginalName;
					String suffix = orginalName.substring(orginalName.lastIndexOf(".") + 1, orginalName.length());
					File file = new File(rootPath + filePath);
		            if (file.exists()) {
		                file.delete();
		                file.mkdirs();
		            }else {
		                file.mkdirs();
		            }
		            mFile.transferTo(file);
					if ("xls".equals(suffix) || "XLS".equals(suffix)) {
		               secUserList = importXls(file);
		            }else if ("xlsx".equals(suffix) || "XLSX".equals(suffix)) {
		                secUserList = importXlsx(file);
		            }
				} catch (Exception e) {
				e.printStackTrace();
			}		
			return secUserList;
	}
	
	
	/**
	 * 导入用户信息
	 */
	@RequestMapping("/importUser.do")
	@ResponseBody
	public Msg importUser(@RequestParam(value="uploadFile")MultipartFile mFile, HttpServletRequest request, HttpServletResponse response) {
		String rootPath = request.getSession().getServletContext().getRealPath(File.separator);
		List<UserForm> secUserList = new ArrayList<UserForm>();
			String userId = WebContextUtil.getUserId();
			try {
					String orginalName = mFile.getOriginalFilename();
					 String filePath = "uploadFile/" +userId + orginalName;
					String suffix = orginalName.substring(orginalName.lastIndexOf(".") + 1, orginalName.length());
					File file = new File(rootPath + filePath);
		            if (file.exists()) {
		                file.delete();
		                file.mkdirs();
		            }else {
		                file.mkdirs();
		            }
		            mFile.transferTo(file);
					if ("xls".equals(suffix) || "XLS".equals(suffix)) {
		               secUserList = importXls(file);
		                userService.importFile(secUserList);
		            }else if ("xlsx".equals(suffix) || "XLSX".equals(suffix)) {
		                secUserList = importXlsx(file);
		                userService.importFile(secUserList);
		            }
					return new Msg(true, "信息上传成功");
				} catch (Exception e) {
				e.printStackTrace();
			}
		return new Msg(false, "信息上传异常");
	}


	//导入后缀名问.xls文档
	private List<UserForm> importXls(File file) {
        List<UserForm> secUserList = new ArrayList<UserForm>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        InputStream is = null;
        HSSFWorkbook hWorkbook = null;
        try {
            is = new FileInputStream(file);
            hWorkbook = new HSSFWorkbook(is);
            HSSFSheet hSheet = hWorkbook.getSheetAt(0);
            
            if (null != hSheet){  
                for (int i = 1; i < hSheet.getPhysicalNumberOfRows(); i++){  
                	UserForm su = new UserForm();
                    HSSFRow hRow = hSheet.getRow(i);
                    if(!StringUtil.isEmpty(hRow.getCell(1).toString())){
                        su.setArea(hRow.getCell(1).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(2).toString())){
                        OrgForm orgform=orgService.getId(hRow.getCell(2).toString());
                        su.setOrgId(orgform.getId());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(3).toString())){
                        OrgForm orgform2=orgService.getId(hRow.getCell(3).toString());
                        su.setOrgChildId(orgform2.getId());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(4).toString())){
                        su.setAccount(hRow.getCell(4).toString().trim());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(5).toString())){
                        su.setChinaname(hRow.getCell(5).toString().trim());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(6).toString())){
                        su.setName(hRow.getCell(6).toString().trim());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(7).toString())){
                        PositionForm positionform=posService.getId(hRow.getCell(7).toString());
                        su.setPositionId(positionform.getId());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(8).toString())){
                        su.setPositionEng(hRow.getCell(8).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(9).toString())){
                        su.setSex(hRow.getCell(9).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(10).toString())){
                        if(hRow.getCell(10).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(10))){ 
                        		su.setBirthday(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(10).getNumericCellValue())).toString());
                        	}
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(11).toString())){
                        String ageString=hRow.getCell(11).toString();
                        su.setAge(ageString.substring(0, ageString.lastIndexOf('.')));
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(12).toString())){
                        if(hRow.getCell(12).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(12))){ 
                        		su.setSecurityDate(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(12).getNumericCellValue())).toString());
                        	}else{
                            	su.setSecurityDate(hRow.getCell(12).toString());
                            }
                        }else{
                        	su.setSecurityDate(hRow.getCell(12).toString());
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(13).toString())){
                        if(hRow.getCell(13).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(13))){ 
                        		su.setFundDate(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(13).getNumericCellValue())).toString());
                        	}else{
                            	su.setFundDate(hRow.getCell(13).toString());
                            }
                        }else{
                        	su.setFundDate(hRow.getCell(13).toString());
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(14).toString())){
                        if(hRow.getCell(14).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(14))){ 
                        		su.setEmploymentStr(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(14).getNumericCellValue())).toString());
                        	}
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(15).toString())){
                        su.setWorkAge(hRow.getCell(15).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(16).toString())){
                        su.setProbationLimit(hRow.getCell(16).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(17).toString())){
                        if(hRow.getCell(17).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(17))){ 
                        		su.setProbationEnd(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(17).getNumericCellValue())).toString());
                        	}
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(18).toString())){
                        if(hRow.getCell(18).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(18))){ 
                        		su.setBecomeStaffDate(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(18).getNumericCellValue())).toString());
                        	}
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(19).toString())){
                        su.setAgreementLimit(hRow.getCell(19).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(20).toString())){
                        if(hRow.getCell(20).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(20))){ 
                        		su.setAgreementStartDate(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(20).getNumericCellValue())).toString());
                        	}
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(21).toString())){
                        if(hRow.getCell(21).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(21))){ 
                        		su.setAgreementEndDate(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(21).getNumericCellValue())).toString());
                        	}
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(22).toString())){
                        if(!StringUtil.isEmpty(hRow.getCell(22).toString())){
                        su.setAgreementTimes(hRow.getCell(22).toString().substring(0,hRow.getCell(22).toString().length()-2));
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(23).toString())){
                        su.setMarriage(hRow.getCell(23).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(24).toString())){
                        su.setBear(hRow.getCell(24).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(25).toString())){
                        su.setNation(hRow.getCell(25).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(26).toString())){
                        su.setPoliticalFace(hRow.getCell(26).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(27).toString())){
                        su.setOrigin(hRow.getCell(27).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(28).toString())){
                        su.setAccountAddr(hRow.getCell(28).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(29).toString())){
                        su.setAccountPro(hRow.getCell(29).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(30).toString())){
                        su.setAddress(hRow.getCell(30).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(31).toString())){
                        if(!StringUtil.isEmpty(hRow.getCell(31).toString())){
                        	su.setPhone(new BigDecimal(hRow.getCell(31).toString()).toPlainString());
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(32).toString())){
                        su.setIdcard(hRow.getCell(32).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(33).toString())){
                        su.setSchool(hRow.getCell(33).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(34).toString())){
                        if(hRow.getCell(34).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(34))){ 
                        		su.setGraduation(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(34).getNumericCellValue())).toString());
                        	}
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(35).toString())){
                        su.setProfession(hRow.getCell(35).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(36).toString())){
                        su.setDegree(hRow.getCell(36).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(37).toString())){
                        su.setCertificate(hRow.getCell(37).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(38).toString())){
                        su.setContact(hRow.getCell(38).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(39).toString())){
                        if(!StringUtil.isEmpty(hRow.getCell(39).toString())){
                        	 su.setContactPhone(new BigDecimal(hRow.getCell(39).toString()).toPlainString());
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(40).toString())){
                        su.setWorkOld(hRow.getCell(40).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(41).toString())){
                        su.setMaterial(hRow.getCell(41).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(42).toString())){
                        //su.setBankCard(new BigDecimal(xRow.getCell(42).toString()).toPlainString());
                        su.setBankCard(hRow.getCell(42).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(43).toString())){
                        su.setTrain(hRow.getCell(43).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(44).toString())){
                        //su.setSecurityCard(new BigDecimal(xRow.getCell(44).toString()).toPlainString());
                        su.setSecurityCard(hRow.getCell(44).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(45).toString())){
                        su.setFund(hRow.getCell(45).toString());
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(46).toString())){
                        if(hRow.getCell(46).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(46))){ 
                        		su.setLeaveDate(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(46).getNumericCellValue())).toString());
                        	}
                        }
                        }
                        if(!StringUtil.isEmpty(hRow.getCell(47).toString())){
                        su.setEmail(hRow.getCell(47).toString());
                        }
                        /*if(!StringUtil.isEmpty(hRow.getCell(48).toString())){
                        su.setRole_ids(this.roleService.getRoleId(hRow.getCell(48).toString()));
                        }*/
                    secUserList.add(su);
                }  
            }  
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (null != hWorkbook) {
                try {
                  //  hWorkbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }    
        
        return secUserList;
    }
    
	//导入后缀名问.xlsx文档
    private List<UserForm> importXlsx(File file) {
        List<UserForm> secUserList = new ArrayList<UserForm>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        DecimalFormat df = new DecimalFormat("#");
        InputStream is = null;
        XSSFWorkbook  xWorkbook = null;
        try {
            is = new FileInputStream(file);
            xWorkbook = new XSSFWorkbook(is);
            XSSFSheet xSheet = xWorkbook.getSheetAt(0);
            
            if (null != xSheet) {
                for (int i = 1; i < xSheet.getPhysicalNumberOfRows(); i++) {
                	UserForm su = new UserForm();
                    XSSFRow xRow = xSheet.getRow(i); 
                    if(!StringUtil.isEmpty(xRow.getCell(1).toString())){
                    su.setArea(xRow.getCell(1).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(2).toString())){
                    OrgForm orgform=orgService.getId(xRow.getCell(2).toString());
                    su.setOrgId(orgform.getId());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(3).toString())){
                    OrgForm orgform2=orgService.getId(xRow.getCell(3).toString());
                    su.setOrgChildId(orgform2.getId());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(4).toString())){
                    su.setAccount(xRow.getCell(4).toString().trim());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(5).toString())){
                    su.setChinaname(xRow.getCell(5).toString().trim());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(6).toString())){
                    su.setName(xRow.getCell(6).toString().trim());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(7).toString())){
                    PositionForm positionform=posService.getId(xRow.getCell(7).toString());
                    su.setPositionId(positionform.getId());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(8).toString())){
                    su.setPositionEng(xRow.getCell(8).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(9).toString())){
                    su.setSex(xRow.getCell(9).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(10).toString())){
                    if(xRow.getCell(10).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(10))){ 
                    		su.setBirthday(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(10).getNumericCellValue())).toString());
                    	}
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(11).getRawValue())){
                    String ageString=xRow.getCell(11).getRawValue();
                    su.setAge(xRow.getCell(11).getRawValue());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(12).toString())){
                    if(xRow.getCell(12).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(12))){ 
                    		su.setSecurityDate(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(12).getNumericCellValue())).toString());
                    	}else{
                        	su.setSecurityDate(xRow.getCell(12).toString());
                        }
                    }else{
                    	su.setSecurityDate(xRow.getCell(12).toString());
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(13).toString())){
                    if(xRow.getCell(13).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(13))){ 
                    		su.setFundDate(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(13).getNumericCellValue())).toString());
                    	}else{
                        	su.setFundDate(xRow.getCell(13).toString());
                        }
                    }else{
                    	su.setFundDate(xRow.getCell(13).toString());
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(14).toString())){
                    if(xRow.getCell(14).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(14))){ 
                    		su.setEmploymentStr(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(14).getNumericCellValue())).toString());
                    	}
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(15).toString())){
                    su.setWorkAge(xRow.getCell(15).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(16).toString())){
                    su.setProbationLimit(xRow.getCell(16).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(17).toString())){
                    if(xRow.getCell(17).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(17))){ 
                    		su.setProbationEnd(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(17).getNumericCellValue())).toString());
                    	}
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(18).toString())){
                    if(xRow.getCell(18).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(18))){ 
                    		su.setBecomeStaffDate(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(18).getNumericCellValue())).toString());
                    	}
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(19).toString())){
                    su.setAgreementLimit(xRow.getCell(19).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(20).toString())){
                    if(xRow.getCell(20).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(20))){ 
                    		su.setAgreementStartDate(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(20).getNumericCellValue())).toString());
                    	}
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(21).toString())){
                    if(xRow.getCell(21).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(21))){ 
                    		su.setAgreementEndDate(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(21).getNumericCellValue())).toString());
                    	}
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(22).toString())){
                    if(!StringUtil.isEmpty(xRow.getCell(22).toString())){
                    su.setAgreementTimes(xRow.getCell(22).toString().substring(0,xRow.getCell(22).toString().length()-2));
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(23).toString())){
                    su.setMarriage(xRow.getCell(23).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(24).toString())){
                    su.setBear(xRow.getCell(24).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(25).toString())){
                    su.setNation(xRow.getCell(25).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(26).toString())){
                    su.setPoliticalFace(xRow.getCell(26).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(27).toString())){
                    su.setOrigin(xRow.getCell(27).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(28).toString())){
                    su.setAccountAddr(xRow.getCell(28).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(29).toString())){
                    su.setAccountPro(xRow.getCell(29).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(30).toString())){
                    su.setAddress(xRow.getCell(30).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(31).toString())){
                    if(!StringUtil.isEmpty(xRow.getCell(31).toString())){
                    	su.setPhone(new BigDecimal(xRow.getCell(31).toString()).toPlainString());
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(32).toString())){
                    su.setIdcard(xRow.getCell(32).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(33).toString())){
                    su.setSchool(xRow.getCell(33).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(34).toString())){
                    if(xRow.getCell(34).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(34))){ 
                    		su.setGraduation(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(34).getNumericCellValue())).toString());
                    	}
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(35).toString())){
                    su.setProfession(xRow.getCell(35).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(36).toString())){
                    su.setDegree(xRow.getCell(36).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(37).toString())){
                    su.setCertificate(xRow.getCell(37).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(38).toString())){
                    su.setContact(xRow.getCell(38).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(39).toString())){
                    if(!StringUtil.isEmpty(xRow.getCell(39).toString())){
                    	 su.setContactPhone(new BigDecimal(xRow.getCell(39).toString()).toPlainString());
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(40).toString())){
                    su.setWorkOld(xRow.getCell(40).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(41).toString())){
                    su.setMaterial(xRow.getCell(41).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(42).toString())){
                    //su.setBankCard(new BigDecimal(xRow.getCell(42).toString()).toPlainString());
                    su.setBankCard(xRow.getCell(42).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(43).toString())){
                    su.setTrain(xRow.getCell(43).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(44).getRawValue())){
                    //su.setSecurityCard(new BigDecimal(xRow.getCell(44).toString()).toPlainString());
                    su.setSecurityCard(xRow.getCell(44).getRawValue());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(45).getRawValue())){
                    su.setFund(xRow.getCell(45).toString());
                    }
                    
                    if(!StringUtil.isEmpty(xRow.getCell(46).toString())){
                    if(xRow.getCell(46).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(46))){ 
                    		su.setLeaveDate(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(46).getNumericCellValue())).toString());
                    	}
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(47).toString())){
                    su.setEmail(xRow.getCell(47).toString());
                    }
                   /* if(!StringUtil.isEmpty(xRow.getCell(48).toString())){
                    su.setRole_ids(this.roleService.getRoleId(xRow.getCell(48).toString()));
                    }*/
                    secUserList.add(su);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (null != xWorkbook) {
                try {
                   // xWorkbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return secUserList;
    }
	
    /**
	 * 查询部门用户信息
	 */
	@RequestMapping("/orgdatagridperson.do")
	@ResponseBody
	public DataGrid orgdatagridperson(HttpSession session,UserForm form) throws Exception {
		return this.userService.orgdatagridperson(session,form);
	}
	
	/**
	 * 查询用户个人信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/datagridpersonal.do")
	@ResponseBody
	public DataGrid datagridpersonal() throws Exception {
		return this.userService.datagridpersonal();
	}
	
	@RequestMapping("/importUserId.do")
	@ResponseBody
	public int equlasVal(String account) throws Exception {
		return this.userService.equlasValAccount(account);
	}
	
	
	@RequestMapping("notice.do")
	@ResponseBody
	public Msg notice(UserForm form){
		return userService.notice(form.getId());
	}
	
	@RequestMapping("selectCurUser.do")
	@ResponseBody
	public UserForm selectCurUser(){
		return userService.selectCurUser();
	}
	
	@RequestMapping("chargeTree.do")
	@ResponseBody
	public List<UserForm> chargeTree(){
		return userService.chargeTree();
	}
} 
