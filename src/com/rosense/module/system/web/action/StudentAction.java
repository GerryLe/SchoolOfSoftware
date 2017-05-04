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
import com.rosense.module.system.service.IStudentService;
import com.rosense.module.system.web.form.ClassForm;
import com.rosense.module.system.web.form.PositionForm;
import com.rosense.module.system.web.form.RoleForm;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/student")
public class StudentAction extends BaseController {
	@Inject
	private IStudentService stuService;
	@Inject
	private IClassService classService;
	@Inject
	private IPositionService posService;
	@Inject
	private IRoleService roleService;
	
	@RequestMapping("/userrole_main_UI.do")
	public String userrole_main_UI(String id, Model model) {
		model.addAttribute("id", id);
		UserForm form = this.stuService.get(id);
		if (form != null) {
			model.addAttribute("userroles", form.getRole_ids());
		}
		return Const.SYSTEM + "userrole_main_UI";
	}

	/**
	 * 添加用户
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(UserForm form) throws Exception {
		return this.stuService.addStu(form);
	}

	/**
	 * 删除用户
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(UserForm form) throws Exception {
		return this.stuService.delete(form);
	}

	/**
	* 锁定账号
	*/
	@RequestMapping("/lockUser.do")
	@ResponseBody
	public Msg lockUser(String id) {
		return this.stuService.lockUser(id);
	}

	/**
	 * 重置密码
	 */
	@RequestMapping("/resetPwd.do")
	@ResponseBody
	public Msg resetPwd(String id) {
		return this.stuService.resetPwd(id);
	}

	/**
	 * 修改账号
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(UserForm form) throws Exception {
		return this.stuService.update(form);
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public UserForm get(UserForm form) throws Exception {
		return this.stuService.get(form.getId());
	}

	/**
	 * 查询账号
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(UserForm form,String selectType,String searchKeyName) throws Exception {
		return this.stuService.datagrid(form,selectType,searchKeyName);
	}

	/**
	 * 查询用户信息
	 */
	@RequestMapping("/datagridperson.do")
	@ResponseBody
	public DataGrid datagridperson(UserForm form,String selectType,String searchKeyName) throws Exception {
		return this.stuService.datagridperson(form,selectType,searchKeyName);
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
		return this.stuService.datagrid_ref(form);
	}

	/**
	 * 查询用户
	 */
	@RequestMapping("/search.do")
	@ResponseBody
	public List<UserForm> search(String content) throws Exception {
		return this.stuService.searchUsers(content);
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
					Msg msg = this.stuService.updatePhoto(path + newName);
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
		return this.stuService.updatePwd(form);
	}

	/**
	 * 修改角色
	 */
	@RequestMapping("/updateRole.do")
	@ResponseBody
	public Msg batchRole(UserForm form) {
		return this.stuService.batchUserRole(form);
	}

	/**
	 * 添加角色用户
	 */
	@RequestMapping("/adduserrole.do")
	@ResponseBody
	public Msg no_adduserrole(String roleId, String userIds) throws Exception {
		return this.stuService.addRoleForUser(roleId, userIds);
	}

	/**
	 * 删除角色用户
	 */
	@RequestMapping("/deleteuserrole.do")
	@ResponseBody
	public Msg deleteuserrole(String roleId, String userId) throws Exception {
		return this.stuService.deleteRoleForUser(roleId, userId);
	}

	/**
	 * 添加部门用户
	 */
	@RequestMapping("/adduserorg.do")
	@ResponseBody
	public Msg adduserorg(String orgId, String userIds) throws Exception {
		return this.stuService.addOrgForUser(orgId, userIds);
	}

	/**
	 * 删除部门用户
	 */
	@RequestMapping("/deleteuserorg.do")
	@ResponseBody
	public Msg deleteuserorg(String userId) throws Exception {
		return this.stuService.deleteOrgForUser(userId);
	}

	/**
	 * 添加职位用户
	 */
	@RequestMapping("/adduserposition.do")
	@ResponseBody
	public Msg adduserposition(String positionId, String userIds) throws Exception {
		return this.stuService.addPositionForUser(positionId, userIds);
	}

	/**
	 * 删除职位用户
	 */
	@RequestMapping("/deleteuserposition.do")
	@ResponseBody
	public Msg deleteuserposition(String userId) throws Exception {
		return this.stuService.deletePositionForUser(userId);
	}
	
	
	
	/**
	 * 判断导入学生信息是否重复
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
		                stuService.importFile(secUserList);
		            }else if ("xlsx".equals(suffix) || "XLSX".equals(suffix)) {
		                secUserList = importXlsx(file);
		                stuService.importFile(secUserList);
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
                    if(!StringUtil.isEmpty(hRow.getCell(0).toString())){
                    	ClassForm classform=classService.getId(hRow.getCell(0).toString());
                    	su.setClass_id(classform.getId());
                        }
                    if(!StringUtil.isEmpty(hRow.getCell(1).toString())){
                        if(!StringUtil.isEmpty(hRow.getCell(1).toString())){
                        	su.setStu_no(new BigDecimal(hRow.getCell(1).toString()).toPlainString());
                        }
                      }
                    if(!StringUtil.isEmpty(hRow.getCell(2).toString())){
                         su.setStu_name(hRow.getCell(2).toString());
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(3).toString())){
                        su.setSex(hRow.getCell(3).toString());
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(4).toString())){
                        if(!StringUtil.isEmpty(hRow.getCell(4).toString())){
                        	su.setPhone(new BigDecimal(hRow.getCell(4).toString()).toPlainString());
                        }
                      }
                    if(!StringUtil.isEmpty(hRow.getCell(5).toString())){
                    	String ageString=hRow.getCell(5).toString().trim();
                       su.setCornet(ageString.substring(0, ageString.lastIndexOf('.')));
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(6).toString())){
                    su.setEmail(hRow.getCell(6).toString().trim());
                    }
                    if(hRow.getCell(7).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(7))){ 
                    		su.setBirthday(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(7).getNumericCellValue())).toString());
                    	}
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(8).toString())){
                        su.setIdcard(hRow.getCell(8).toString());
                      }
                    if(!StringUtil.isEmpty(hRow.getCell(9).toString())){
                    su.setProvince(hRow.getCell(9).toString());
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(10).toString())){
                        su.setCity(hRow.getCell(10).toString());
                     }
                    if(hRow.getCell(11).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(11))){ 
                    		su.setEntrance_date_Str(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(11).getNumericCellValue())).toString());
                    	}
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(12).toString())){
                    su.setNation(hRow.getCell(12).toString());
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(13).toString())){
                    su.setPoliticalFace(hRow.getCell(13).toString());
                    }
                    
                    if(!StringUtil.isEmpty(hRow.getCell(14).toString())){
                    su.setOrigin(hRow.getCell(14).toString());
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(15).toString())){
                    su.setAccountAddr(hRow.getCell(15).toString());
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(16).toString())){
                    su.setAccountPro(hRow.getCell(16).toString());
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(17).toString())){
                    su.setGraduate_school(hRow.getCell(17).toString());
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(18).toString())){
                        su.setProfession(hRow.getCell(18).toString());
                     }
                    if(!StringUtil.isEmpty(hRow.getCell(19).toString())){
                    su.setContact(hRow.getCell(19).toString());
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(20).toString())){
                    if(!StringUtil.isEmpty(hRow.getCell(20).toString())){
                    	 su.setContactPhone(new BigDecimal(hRow.getCell(20).toString()).toPlainString());
                    }
                    }
                    if(!StringUtil.isEmpty(hRow.getCell(21).toString())){
                    su.setMaterial(hRow.getCell(21).toString());
                    }
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
                    if(!StringUtil.isEmpty(xRow.getCell(0).toString())){
                    	ClassForm classform=classService.getId(xRow.getCell(0).toString());
                    	su.setClass_id(classform.getId());
                        }
                    if(!StringUtil.isEmpty(xRow.getCell(1).toString())){
                        if(!StringUtil.isEmpty(xRow.getCell(1).toString())){
                        	su.setStu_no(new BigDecimal(xRow.getCell(1).toString()).toPlainString());
                        }
                      }
                    if(!StringUtil.isEmpty(xRow.getCell(2).toString())){
                         su.setStu_name(xRow.getCell(2).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(3).toString())){
                        su.setSex(xRow.getCell(3).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(4).toString())){
                        if(!StringUtil.isEmpty(xRow.getCell(4).toString())){
                        	su.setPhone(new BigDecimal(xRow.getCell(4).toString()).toPlainString());
                        }
                      }
                    if(!StringUtil.isEmpty(xRow.getCell(5).toString())){
                    	String ageString=xRow.getCell(5).toString().trim();
                        su.setCornet(ageString.substring(0, ageString.lastIndexOf('.')));
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(6).toString())){
                    su.setEmail(xRow.getCell(6).toString().trim());
                    }
                    if(xRow.getCell(7).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(7))){ 
                    		su.setBirthday(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(7).getNumericCellValue())).toString());
                    	}
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(8).toString())){
                        su.setIdcard(xRow.getCell(8).toString());
                      }
                    if(!StringUtil.isEmpty(xRow.getCell(9).toString())){
                    su.setProvince(xRow.getCell(9).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(10).toString())){
                        su.setCity(xRow.getCell(10).toString());
                     }
                    if(xRow.getCell(11).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(11))){ 
                    		su.setEntrance_date_Str(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(11).getNumericCellValue())).toString());
                    	}
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(12).toString())){
                    su.setNation(xRow.getCell(12).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(13).toString())){
                    su.setPoliticalFace(xRow.getCell(13).toString());
                    }
                    
                    if(!StringUtil.isEmpty(xRow.getCell(14).toString())){
                    su.setOrigin(xRow.getCell(14).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(15).toString())){
                    su.setAccountAddr(xRow.getCell(15).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(16).toString())){
                    su.setAccountPro(xRow.getCell(16).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(17).toString())){
                    su.setGraduate_school(xRow.getCell(17).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(18).toString())){
                        su.setProfession(xRow.getCell(18).toString());
                     }
                    if(!StringUtil.isEmpty(xRow.getCell(19).toString())){
                    su.setContact(xRow.getCell(19).toString());
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(20).toString())){
                    if(!StringUtil.isEmpty(xRow.getCell(20).toString())){
                    	 su.setContactPhone(new BigDecimal(xRow.getCell(20).toString()).toPlainString());
                    }
                    }
                    if(!StringUtil.isEmpty(xRow.getCell(21).toString())){
                    su.setMaterial(xRow.getCell(21).toString());
                    }
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
	 * 查询用户个人信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/datagridpersonal.do")
	@ResponseBody
	public DataGrid datagridpersonal() throws Exception {
		return this.stuService.datagridpersonal();
	}
	
	@RequestMapping("/importUserId.do")
	@ResponseBody
	public int equlasVal(String account) throws Exception {
		return this.stuService.equlasValAccount(account);
	}
	
	
	
	@RequestMapping("selectCurUser.do")
	@ResponseBody
	public UserForm selectCurUser(){
		return stuService.selectCurUser();
	}
	
	@RequestMapping("chargeTree.do")
	@ResponseBody
	public List<UserForm> chargeTree(){
		return stuService.chargeTree();
	}
} 
