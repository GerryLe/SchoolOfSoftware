package com.rosense.module.system.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.ImageUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.service.IClassService;
import com.rosense.module.system.service.IRoleService;
import com.rosense.module.system.service.IStudentService;
import com.rosense.module.system.web.form.ClassForm;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/student")
public class StudentAction extends BaseController {
	@Inject
	private IStudentService stuService;
	@Inject
	private IClassService classService;
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
	 * 修改用户
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(UserForm form) throws Exception {
		return this.stuService.update(form);
	}

	
	/**
	 * 删除用户
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(UserForm form) throws Exception {
		return this.stuService.delete(form);
	}

	
	@RequestMapping("/get.do")
	@ResponseBody
	public UserForm get(UserForm form) throws Exception {
		return this.stuService.get(form.getId());
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
	 * 查询用户
	 */
	@RequestMapping("/search.do")
	@ResponseBody
	public List<UserForm> search(String content) throws Exception {
		return this.stuService.searchUsers(content);
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
	
} 
