package com.rosense.module.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.rosense.basic.dao.ExtFieldEntity;

/**
 * 附件文件
 * @author Can-Dao
 * 	
 * 2015年8月29日 下午1:36:00
 *
 */
@Table(name = "simple_attach")
@Entity
public class AttachEntity extends ExtFieldEntity {
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
