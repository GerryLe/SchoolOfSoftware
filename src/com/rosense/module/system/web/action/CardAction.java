package com.rosense.module.system.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.service.ICardService;
import com.rosense.module.system.web.form.CardForm;

@Controller
@RequestMapping("/admin/system/card")
public class CardAction extends BaseController {
	@Inject
	private ICardService cardService;

	/**
	 * 查询用户信息
	 */
	@RequestMapping("/datagridcard.do")
	@ResponseBody
	public DataGrid datagridperson(CardForm form) throws Exception {
		return this.cardService.datagridcard(form);
	}

	/**
	 * 部门主管查询用户信息
	 */
	@RequestMapping("/orgdatagridcard.do")
	@ResponseBody
	public DataGrid orgdatagridperson(HttpSession session,CardForm form) throws Exception {
		return this.cardService.orgdatagridcard(session,form);
	}
	
	/**
	 * 导入用户打卡记录信息
	 */
	@RequestMapping("/importCard.do")
	@ResponseBody
	public Msg importUser(@RequestParam(value="uploadFile")MultipartFile mFile, HttpServletRequest request, HttpServletResponse response) {
		
		String rootPath = request.getSession().getServletContext().getRealPath(File.separator);
		List<CardForm> cardList = new ArrayList<CardForm>();
			String userId = WebContextUtil.getUserId();
			try {
					String orginalName = mFile.getOriginalFilename();
					String filePath = "uploadCardFile/" +userId + orginalName;
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
		               cardList = importXls(file);
		                cardService.importFile(cardList);
		            }else if ("xlsx".equals(suffix) || "XLSX".equals(suffix)) {
		                cardList = importXlsx(file);
		                cardService.importFile(cardList);
		            }
					return new Msg(true, "信息上传成功");
				} catch (Exception e) {
				e.printStackTrace();
			}
		return new Msg(false, "信息上传异常");
	}

	
	private List<CardForm> importXls(File file) {
        List<CardForm> secUserList = new ArrayList<CardForm>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sd = new SimpleDateFormat("hh:mm");
        InputStream is = null;
        HSSFWorkbook hWorkbook = null;
        try {
            is = new FileInputStream(file);
            hWorkbook = new HSSFWorkbook(is);
            HSSFSheet hSheet = hWorkbook.getSheetAt(0);
            
            if (null != hSheet){  
                for (int i = 1; i < hSheet.getPhysicalNumberOfRows(); i++){  
                	CardForm su = new CardForm();
                    HSSFRow hRow = hSheet.getRow(i);
                    
                    su.setAccount(hRow.getCell(0).toString());
                    su.setName(hRow.getCell(1).toString());
                    if(hRow.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(2))){ 
                    		su.setRecordDate(sdf.format(HSSFDateUtil.getJavaDate(hRow.getCell(2).getNumericCellValue())).toString());
                      }
                    }
                    else{
                    	 su.setRecordDate(hRow.getCell(2).toString());
                    }
                    
                    if(hRow.getCell(3).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(3))){ 
                    		su.setStartTime(sd.format(HSSFDateUtil.getJavaDate(hRow.getCell(3).getNumericCellValue())).toString());
                      }
                    }
                    else{
                    	su.setStartTime(hRow.getCell(3).toString());
                    }
                    
                    if(hRow.getCell(4).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(4))){ 
                    		su.setEndTime(sd.format(HSSFDateUtil.getJavaDate(hRow.getCell(4).getNumericCellValue())).toString());
                      }
                    }
                    else{
                    	su.setEndTime(hRow.getCell(4).toString());
                    }
                    
                    if(hRow.getCell(5).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(hRow.getCell(5))){ 
                    		su.setLateTime(sd.format(HSSFDateUtil.getJavaDate(hRow.getCell(5).getNumericCellValue())).toString());
                      }
                    }
                    else{
                    	su.setLateTime(hRow.getCell(5).toString());
                    }
                    su.setOrgName(hRow.getCell(6).toString());
                    su.setPositionName(hRow.getCell(7).toString());
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
    
    private List<CardForm> importXlsx(File file) {
        List<CardForm> secUserList = new ArrayList<CardForm>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sd = new SimpleDateFormat("hh:mm");
        InputStream is = null;
        XSSFWorkbook  xWorkbook = null;
        try {
            is = new FileInputStream(file);
            xWorkbook = new XSSFWorkbook(is);
            XSSFSheet xSheet = xWorkbook.getSheetAt(0);
            
            if (null != xSheet) {
                for (int i = 1; i < xSheet.getPhysicalNumberOfRows(); i++) {
                	CardForm su = new CardForm();
                    XSSFRow xRow = xSheet.getRow(i);

                    su.setAccount(xRow.getCell(0).toString());
                    su.setName(xRow.getCell(1).toString());
                    if(xRow.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(2))){ 
                    		su.setRecordDate(sdf.format(HSSFDateUtil.getJavaDate(xRow.getCell(2).getNumericCellValue())).toString());
                      }
                    }
                    else{
                    	 su.setRecordDate(xRow.getCell(2).toString());
                    }
                    
                    if(xRow.getCell(3).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(3))){ 
                    		su.setStartTime(sd.format(HSSFDateUtil.getJavaDate(xRow.getCell(3).getNumericCellValue())).toString());
                      }
                    }
                    else{
                    	su.setStartTime(xRow.getCell(3).toString());
                    }
                    
                    if(xRow.getCell(4).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(4))){ 
                    		su.setEndTime(sd.format(HSSFDateUtil.getJavaDate(xRow.getCell(4).getNumericCellValue())).toString());
                      }
                    }
                    else{
                    	su.setEndTime(xRow.getCell(4).toString());
                    }
                    
                    if(xRow.getCell(5).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    	if(HSSFDateUtil.isCellDateFormatted(xRow.getCell(5))){ 
                    		su.setLateTime(sd.format(HSSFDateUtil.getJavaDate(xRow.getCell(5).getNumericCellValue())).toString());
                      }
                    }
                    else{
                    	su.setLateTime(xRow.getCell(5).toString());
                    }
                
                    su.setOrgName(xRow.getCell(6).toString());
                    su.setPositionName(xRow.getCell(7).toString());
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
}
