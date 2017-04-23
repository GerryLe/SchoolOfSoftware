package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.PageHelper;

/**
 * 附件
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public class AttachForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pid;//关联ID
	private String fileName;//文件名
	private String filePath;//路径
	private String fileType;//文件类型
	private String orginalName;//旧名字

	public void setOrginalName(String orginalName) {
		this.orginalName = orginalName;
	}

	public String getOrginalName() {
		return orginalName;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
