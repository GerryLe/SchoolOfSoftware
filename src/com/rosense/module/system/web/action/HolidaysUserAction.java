package com.rosense.module.system.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.service.IHolidaysUserService;
import com.rosense.module.system.web.form.HolidaysUserForm;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/holidaysuser")
public class HolidaysUserAction extends BaseController {
	
	@Inject
	private IHolidaysUserService holidaysUserService;
	
	@RequestMapping("/get.do")
	@ResponseBody
	public DataGrid get(HolidaysUserForm form) throws Exception {
			return this.holidaysUserService.get(form);

	}
	
	@RequestMapping("/select.do")
	@ResponseBody
	public HolidaysUserForm select(String id) throws Exception {
			return this.holidaysUserService.select(id);

	}
	
	
	@RequestMapping("/importUserId.do")
	@ResponseBody
	public int equlasVal(String account) throws Exception {
		return this.holidaysUserService.equlasValAccount(account);
	}
	
	
	/**
	 * 判断导入用户信息是否重复
	 */
	@RequestMapping("/checkUserAccount.do")
	@ResponseBody
	public List<HolidaysUserForm> importUserCheck(@RequestParam(value="uploadFile")MultipartFile mFile, HttpServletRequest request, HttpServletResponse response) {
		String rootPath = request.getSession().getServletContext().getRealPath(File.separator);
		List<HolidaysUserForm> secUserList = new ArrayList<HolidaysUserForm>();
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
	@RequestMapping("/import.do")
	@ResponseBody
	public Msg importUser(@RequestParam(value="uploadFile")MultipartFile mFile, HttpServletRequest request, HttpServletResponse response) {
		String rootPath = request.getSession().getServletContext().getRealPath(File.separator);
		List<HolidaysUserForm> secList = new ArrayList<HolidaysUserForm>();
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
		               secList = importXls(file);
		                holidaysUserService.importFile(secList);
		            }else if ("xlsx".equals(suffix) || "XLSX".equals(suffix)) {
		                secList = importXlsx(file);
		                holidaysUserService.importFile(secList);
		            }
					return new Msg(true, "信息上传成功");
				} catch (Exception e) {
				e.printStackTrace();
			}
		return new Msg(false, "信息上传异常");
	}

	private List<HolidaysUserForm> importXls(File file) {
        List<HolidaysUserForm> secList = new ArrayList<HolidaysUserForm>();
        InputStream is = null;
        HSSFWorkbook hWorkbook = null;
        try {
            is = new FileInputStream(file);
            hWorkbook = new HSSFWorkbook(is);
            HSSFSheet hSheet = hWorkbook.getSheetAt(0);
            
            if (null != hSheet){  
                for (int i = 1; i < hSheet.getPhysicalNumberOfRows(); i++){  
                	HolidaysUserForm hu = new HolidaysUserForm();
                    HSSFRow hRow = hSheet.getRow(i);
                    
                    hu.setUseraccount(hRow.getCell(0).toString());
                    hu.setName(hRow.getCell(1).toString());
                    if(hRow.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(2))){ 
                    		hu.setEmploymentDate(HSSFDateUtil.getJavaDate(hRow.getCell(2).getNumericCellValue()));
                    	}
                    }
                    if(hRow.getCell(3).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(3))){ 
                    		hu.setBecomeStaffDate(HSSFDateUtil.getJavaDate(hRow.getCell(3).getNumericCellValue()).toString());
                    	}
                    }
                    hu.setShouldHoliday(Double.parseDouble(hRow.getCell(4).toString()==null?"0":hRow.getCell(4).toString()));
                    hu.setShouldhaveannualleave(Double.parseDouble(hRow.getCell(5).toString()==null?"0":hRow.getCell(5).toString()));
                    hu.setAlreadyAnnualLeave(Double.parseDouble(hRow.getCell(6).toString()==null?"0":hRow.getCell(6).toString()));
                    hu.setTheremainingannualleave(Double.parseDouble(hRow.getCell(7).toString()==null?"0":hRow.getCell(7).toString()));
                    hu.setLastyearsremainingSiLingfalse(Double.parseDouble(hRow.getCell(8).toString()==null?"0":hRow.getCell(8).toString()));
                    hu.setThisyearshouldbeSiLingfalse(Double.parseDouble(hRow.getCell(9).toString()==null?"0":hRow.getCell(9).toString()));
                    hu.setAlreadySiLingFalse(Double.parseDouble(hRow.getCell(10).toString()==null?"0":hRow.getCell(10).toString()));
                    hu.setTheremainingSiLingfalse(Double.parseDouble(hRow.getCell(11).toString()==null?"0":hRow.getCell(11).toString()));
                    hu.setResidueHoliday(Double.parseDouble(hRow.getCell(12).toString()==null?"0":hRow.getCell(12).toString()));
                    hu.setThismonthhasbeenonmedicalleave(Double.parseDouble(hRow.getCell(13).toString()==null?"0":hRow.getCell(13).toString()));
                    hu.setThisyearsdaysworkovertime(Double.parseDouble(hRow.getCell(14).toString()==null?"0":hRow.getCell(14).toString()));
                    hu.setLastyearsremainingpaidleave(Double.parseDouble(hRow.getCell(15).toString()==null?"0":hRow.getCell(15).toString()));
                    hu.setTheremainingpaidleave(Double.parseDouble(hRow.getCell(16).toString()==null?"0":hRow.getCell(16).toString()));
                    hu.setNotvalidonanannualbasis(Double.parseDouble(hRow.getCell(17).toString()==null?"0":hRow.getCell(17).toString()));
                    
                    secList.add(hu);
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
        
        return secList;
    }
	 private List<HolidaysUserForm> importXlsx(File file) {
	        List<HolidaysUserForm> secList = new ArrayList<HolidaysUserForm>();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	        InputStream is = null;
	        XSSFWorkbook  xWorkbook = null;
	        try {
	            is = new FileInputStream(file);
	            xWorkbook = new XSSFWorkbook(is);
	            XSSFSheet xSheet = xWorkbook.getSheetAt(0);
	            
	            if (null != xSheet) {
	                for (int i = 1; i < xSheet.getPhysicalNumberOfRows(); i++) {
	                	HolidaysUserForm hu = new HolidaysUserForm();
	                    XSSFRow xRow = xSheet.getRow(i); 
	                   
	                    hu.setUseraccount(xRow.getCell(0).toString());
	                    hu.setName(xRow.getCell(1).toString());
	                    if(xRow.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(2))){ 
	                    		hu.setEmploymentDate(HSSFDateUtil.getJavaDate(xRow.getCell(2).getNumericCellValue()));
	                    	}
	                    }
	                    
	                    if(xRow.getCell(3).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                        	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(3))){ 
                        		hu.setBecomeStaffDate(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(3).getNumericCellValue())).toString());
                        	}else{
                        		hu.setBecomeStaffDate(xRow.getCell(3).toString());
                            }
                        }else{
                        	hu.setBecomeStaffDate(xRow.getCell(3).toString());
                        }
	                    
	                    //hu.setBecomeStaffDate(xRow.getCell(3).toString());
	                    hu.setShouldHoliday(Double.parseDouble(xRow.getCell(4).getRawValue()==null?"0":xRow.getCell(4).getRawValue()));
	                    hu.setShouldhaveannualleave(Double.parseDouble(xRow.getCell(5).getRawValue()==null?"0":xRow.getCell(5).getRawValue()));
	                    hu.setAlreadyAnnualLeave(Double.parseDouble(xRow.getCell(6).getRawValue()==null?"0":xRow.getCell(6).getRawValue()));
	                    hu.setTheremainingannualleave(Double.parseDouble(xRow.getCell(7).getRawValue()==null?"0":xRow.getCell(7).getRawValue()));
	                    hu.setLastyearsremainingSiLingfalse(Double.parseDouble(xRow.getCell(8).getRawValue()==null?"0":xRow.getCell(8).getRawValue()));
	                    hu.setThisyearshouldbeSiLingfalse(Double.parseDouble(xRow.getCell(9).getRawValue()==null?"0":xRow.getCell(9).getRawValue()));
	                    hu.setAlreadySiLingFalse(Double.parseDouble(xRow.getCell(10).getRawValue()==null?"0":xRow.getCell(10).getRawValue()));
	                    hu.setTheremainingSiLingfalse(Double.parseDouble(xRow.getCell(11).getRawValue()==null?"0":xRow.getCell(11).getRawValue()));
	                    hu.setResidueHoliday(Double.parseDouble(xRow.getCell(12).getRawValue()==null?"0":xRow.getCell(12).getRawValue()));
	                    hu.setThismonthhasbeenonmedicalleave(Double.parseDouble(xRow.getCell(13).getRawValue()==null?"0":xRow.getCell(13).getRawValue()));
	                    hu.setThisyearsdaysworkovertime(Double.parseDouble(xRow.getCell(14).getRawValue()==null?"0":xRow.getCell(14).getRawValue()));
	                    hu.setLastyearsremainingpaidleave(Double.parseDouble(xRow.getCell(15).getRawValue()==null?"0":xRow.getCell(15).getRawValue()));
	                    hu.setTheremainingpaidleave(Double.parseDouble(xRow.getCell(16).getRawValue()==null?"0":xRow.getCell(16).getRawValue()));
	                    hu.setNotvalidonanannualbasis(Double.parseDouble(xRow.getCell(17).getRawValue()==null?"0":xRow.getCell(17).getRawValue()));
	                    
	                    secList.add(hu);
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
	        
	        return secList;
	    }
	 
	 @RequestMapping("getCurrentUserDefaultRole.do")
		@ResponseBody
		public int getCurrentUserDefaultRole(){
			return holidaysUserService.getCurrentUserDefaultRole();
		}
}
