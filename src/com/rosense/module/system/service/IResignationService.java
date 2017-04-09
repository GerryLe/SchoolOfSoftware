package com.rosense.module.system.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.ResignationForm;

public interface IResignationService {
	/**
	 * 获取审核列表
	 * @param form 获取过滤器参数
	 * @param 
	 * @return
	 */
	DataGrid datagridresignation(ResignationForm form);
	/**
	 * 添加离职申请
	 * @param form
	 * @return
	 */
	Msg add(ResignationForm form);
	/**
	 * 离职申请时获取当前用户基本信息
	 * @param
	 * @return
	 */
	ResignationForm getresignation();
	/**
	 * 审核离职信息
	 * @param form
	 * @return
	 */
	Msg addAudit(ResignationForm form);
	/**
	 * 获取审核信息
	 * @param form 
	 * @return
	 */
	ResignationForm getAudit(ResignationForm form);
	/**
	 * 关键字搜索主管级别以上的员工
	 * @param name 员工英文名
	 * @return
	 */
	List<ResignationForm> getImmediateBoss(String name);
	/**
	 * 检查是否具有审核权限
	 * @param id
	 * @return
	 */
	boolean getcheck(String id);
	/**
	 * 获取审核信息
	 * @param id 离职表id
	 * @return
	 */
	ResignationForm getAuditById(String id);
	/**
	 * 普通员工离职，shadow审核后发送邮件给may和ravic
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * @param str 发送到多个邮箱
	 * @return
	 */
	Msg sendEmail(String title,String content,String[] str);
	/**
	 * 判断用户是否已经申请了离职
	 * @param request
	 * @return
	 */
	boolean getexist();
	/**
	 * 撤销离职申请
	 * @param id 离职表id
	 * @return
	 */
	Msg revocation(String id);
}
