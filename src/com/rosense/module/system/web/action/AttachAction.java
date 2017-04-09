package com.rosense.module.system.web.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rosense.basic.model.Msg;
import com.rosense.basic.util.IOUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.AttachEntity;
import com.rosense.module.system.service.IAttachService;

/**
 * 附件上传管理
 * @author Can-Dao
 * 	
 * 2015年8月24日 下午3:36:32
 *
 */
@Controller
@RequestMapping("/admin/attach/")
public class AttachAction extends BaseController {
	public static final String URL = "/WEB-INF/pages/attach/";
	@Inject
	IAttachService attachService;

	/**
	 * 附件列表
	 * @return
	 */
	@RequestMapping("/attach_list_UI.do")
	public String attach_list_UI(Model model) {
		model.addAttribute("pid", request.getParameter("pid"));
		return URL + "attach_list_UI";
	}

	/**
	 * 附件列表
	 * @return
	 */
	@RequestMapping("/attach_main_UI.do")
	public String attach_main_UI(Model model) {
		model.addAttribute("pid", request.getParameter("pid"));
		return URL + "attach_main_UI";
	}

	/**
	 * 查询列表
	 */
	@RequestMapping("/no_list.do")
	@ResponseBody
	public List<AttachEntity> queryAttachs(String pid) {
		List<AttachEntity> list = attachService.queryAttachs(pid);
		String temp = "png,jpg,gif";
		for (AttachEntity entity : list) {
			//判断是否是图片
			if (temp.contains(entity.getFileType().toLowerCase())) {
				entity.setFileType("img");
				entity.setFilePath(entity.getFilePath().replace("\\", "/"));
				entity.setFilePath(entity.getFilePath().replace("//", "/"));
			}
		}
		return list;
	}

	/**
	 * 上传产品文件
	 * 
	 * @param produceId
	 * @param model
	 * @return
	 */
	@RequestMapping("/no_upload_attach.do")
	@ResponseBody
	public Msg upload_attach(HttpServletRequest request, String pid) {
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String uploadDir = request.getParameter("uploadDir");
			String userId = WebContextUtil.getUserId();
			try {
				uploadDir = (null == uploadDir ? "/temp" : uploadDir);
				String root = multipartRequest.getSession().getServletContext().getRealPath("/");
				uploadDir = StringUtil.filePath(File.separator + Const.uploadDir + uploadDir) + File.separator;
				String savePath = StringUtil.filePath(root + uploadDir + File.separator);
				List<MultipartFile> files = multipartRequest.getFiles(Const.uploadFieldName);
				if (files == null || files.size() < 1) {
					throw new RuntimeException("没有发生上传的文件！请检查");
				}
				String newName = "";
				for (int i = 0; i < files.size(); i++) {
					MultipartFile multipartFile = files.get(i);
					AttachEntity attachEntity = new AttachEntity();
					String orginalName = multipartFile.getOriginalFilename();
					String extension = FilenameUtils.getExtension(orginalName);
					if (null == newName || "".equals(newName.trim())) {
						newName = System.currentTimeMillis() + "." + extension;
					} else if ("orginalName".equals(newName.trim())) {
						newName = orginalName;
					} else {
						newName = newName + "_" + System.currentTimeMillis() + "." + extension;
					}
					FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(savePath + newName));
					attachEntity.setFileName(newName);
					attachEntity.setFilePath(uploadDir + newName);
					attachEntity.setFileType(extension);
					attachEntity.setOrginalName(orginalName);
					attachEntity.setPid(pid);
					attachEntity.setUserId(userId);
					this.attachService.addAttach(attachEntity);
				}
			} catch (IOException e) {
			}
		}
		return new Msg(true, "上传成功");

	}

	/**
	 * 删除附件
	 * 
	 * @param produceId
	 * @param model
	 * @return
	 */
	@RequestMapping("/no_delete_attach.do")
	@ResponseBody
	public Map<String, Object> delupload_File_UI(String id, Model model) {
		boolean status = attachService.deleteAttach(id);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("status", status);
		if (status) {
			data.put("msg", "删除成功");
		} else
			data.put("msg", "删除成功");
		return data;

	}

	/**
	 * 下载附件
	 */
	@RequestMapping("/no_download_attach.do")
	@ResponseBody
	public void download_attach(String id, Model model) {
		AttachEntity fu = attachService.get(id);
		File file = new File(WebContextUtil.getRealPath(fu.getFilePath())); // 要下载的文件绝对路径
		response.reset();
		response.addHeader("Content-Length", "" + file.length());
		try {
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fu.getOrginalName(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		InputStream ins = null;
		OutputStream ous = null;
		try {
			ins = new BufferedInputStream(new FileInputStream(file));
			ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/x-download");
			IOUtils.copyStream(ins, ous);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ins != null)
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			try {
				ous.flush();
				ous.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping("/no_summernote.do")
	@ResponseBody
	public List<String> uploadproductimg(String id) {
		List<String> list = new ArrayList<String>();
		String savePath = WebContextUtil.getAttachedRootPath("summernote");
		String path = WebContextUtil.getAttachedPath("summernote");
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				try {
					File file = new File(savePath);
					file.mkdirs();
				} catch (Exception e) {
				}
				List<MultipartFile> files = multipartRequest.getFiles("files");
				if (files != null && files.size() > 0) {
					for (MultipartFile multipartFile : files) {
						String orginalName = multipartFile.getOriginalFilename();
						String extension = FilenameUtils.getExtension(orginalName);
						String newName = System.currentTimeMillis() + UUID.randomUUID().toString() + "." + extension;
						FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(savePath + newName));
						list.add(path + newName);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

}
