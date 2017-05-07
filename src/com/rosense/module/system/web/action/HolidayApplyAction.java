package com.rosense.module.system.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.ImageUtils;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.service.IHolidayApplysService;
import com.rosense.module.system.service.IUserService;
import com.rosense.module.system.web.form.HolidayApplysForm;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/holidayapplys")
public class HolidayApplyAction extends BaseController {
	@Inject
	private IHolidayApplysService holidayApplysService;
	@Inject
	private IUserService userService;

	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(HolidayApplysForm form ) throws Exception {
		return this.holidayApplysService.add(form);
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(String id) throws Exception {
		return this.holidayApplysService.delete(id);
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(HolidayApplysForm form) throws Exception {
		return this.holidayApplysService.update(form);
	}

	@RequestMapping("/holidayApplysdg.do")
	@ResponseBody
	public DataGrid get(HolidayApplysForm form) throws Exception {
		return this.holidayApplysService.holidayApplysdg(form);

	}

	@RequestMapping("/get.do")
	@ResponseBody
	public HolidayApplysForm get(String id) throws Exception {
		return this.holidayApplysService.get(id);

	}
	
	@RequestMapping("/true.do")
	@ResponseBody
	public Msg approvaltrue(HolidayApplysForm form) throws Exception {
		return this.holidayApplysService.approvaltrue(form);

	}
	
	
	/**
	 * 上传图片
	 */
	@RequestMapping("/updateEnclosure.do")
	@ResponseBody
	public Msg updatephoto() {
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			try {
				String savePath = WebContextUtil.getAttachedRootPath("enclosure");
				new File(savePath).mkdirs();
				String path = WebContextUtil.getAttachedPath("enclosure");
				List<MultipartFile> files = multipartRequest.getFiles(Const.uploadFieldName);
				if (files == null || files.size() < 1) {
					throw new RuntimeException("没有发生上传的文件！请检查");
				}
				if (files.size() > 0) {
					MultipartFile multipartFile = files.get(0);
					String orginalName = multipartFile.getOriginalFilename();
					String extension = FilenameUtils.getExtension(orginalName);
					String newName = UUID.randomUUID().toString()  + "." + extension;
					ImageUtils.transform(multipartFile.getInputStream(), new FileOutputStream(savePath + newName), 1920	, 1080);
					Msg msg = new Msg();
					msg.setObj(path + newName + "?t=" + System.currentTimeMillis());
					return msg;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Msg(false, "照片上传异常");
	}
	
	@RequestMapping("/loading.do")
	@ResponseBody
	public HolidayApplysForm loading(String id) throws Exception {
		return this.holidayApplysService.loading(id);

	}
	
	@RequestMapping("/getLimit.do")
	@ResponseBody
	public Msg getLimit(String id){
		return this.holidayApplysService.getLimit(id);
	}

	
	@RequestMapping("/getaudit.do")
	@ResponseBody
	public Msg getaudit(String id){
		return this.holidayApplysService.getaudit(id);
	}
	
	@RequestMapping("/getDirector.do")
	@ResponseBody
	public List<UserForm> getDirector(String name){
		return this.holidayApplysService.getDirector(name);
	}
}
